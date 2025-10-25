import { MessageTheme } from '../types/theme';

interface RGB {
  r: number;
  g: number;
  b: number;
}

interface HSL {
  h: number;
  s: number;
  l: number;
}

// Convert hex to RGB
function hexToRgb(hex: string): RGB {
  const result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
  return result
    ? {
        r: parseInt(result[1], 16),
        g: parseInt(result[2], 16),
        b: parseInt(result[3], 16),
      }
    : { r: 0, g: 0, b: 0 };
}

// Convert RGB to hex
function rgbToHex(r: number, g: number, b: number): string {
  return '#' + [r, g, b].map(x => {
    const hex = Math.round(Math.max(0, Math.min(255, x))).toString(16);
    return hex.length === 1 ? '0' + hex : hex;
  }).join('');
}

// Convert RGB to HSL
function rgbToHsl(r: number, g: number, b: number): HSL {
  r /= 255;
  g /= 255;
  b /= 255;

  const max = Math.max(r, g, b);
  const min = Math.min(r, g, b);
  let h = 0;
  let s = 0;
  const l = (max + min) / 2;

  if (max !== min) {
    const d = max - min;
    s = l > 0.5 ? d / (2 - max - min) : d / (max + min);

    switch (max) {
      case r:
        h = ((g - b) / d + (g < b ? 6 : 0)) / 6;
        break;
      case g:
        h = ((b - r) / d + 2) / 6;
        break;
      case b:
        h = ((r - g) / d + 4) / 6;
        break;
    }
  }

  return { h: h * 360, s: s * 100, l: l * 100 };
}

// Convert HSL to RGB
function hslToRgb(h: number, s: number, l: number): RGB {
  h /= 360;
  s /= 100;
  l /= 100;

  let r, g, b;

  if (s === 0) {
    r = g = b = l;
  } else {
    const hue2rgb = (p: number, q: number, t: number) => {
      if (t < 0) t += 1;
      if (t > 1) t -= 1;
      if (t < 1 / 6) return p + (q - p) * 6 * t;
      if (t < 1 / 2) return q;
      if (t < 2 / 3) return p + (q - p) * (2 / 3 - t) * 6;
      return p;
    };

    const q = l < 0.5 ? l * (1 + s) : l + s - l * s;
    const p = 2 * l - q;

    r = hue2rgb(p, q, h + 1 / 3);
    g = hue2rgb(p, q, h);
    b = hue2rgb(p, q, h - 1 / 3);
  }

  return { r: r * 255, g: g * 255, b: b * 255 };
}

// Adjust color lightness
function adjustLightness(hex: string, lightnessChange: number): string {
  const rgb = hexToRgb(hex);
  const hsl = rgbToHsl(rgb.r, rgb.g, rgb.b);
  hsl.l = Math.max(0, Math.min(100, hsl.l + lightnessChange));
  const newRgb = hslToRgb(hsl.h, hsl.s, hsl.l);
  return rgbToHex(newRgb.r, newRgb.g, newRgb.b);
}

// Adjust color saturation
function adjustSaturation(hex: string, saturationChange: number): string {
  const rgb = hexToRgb(hex);
  const hsl = rgbToHsl(rgb.r, rgb.g, rgb.b);
  hsl.s = Math.max(0, Math.min(100, hsl.s + saturationChange));
  const newRgb = hslToRgb(hsl.h, hsl.s, hsl.l);
  return rgbToHex(newRgb.r, newRgb.g, newRgb.b);
}

// Shift hue
function shiftHue(hex: string, hueShift: number): string {
  const rgb = hexToRgb(hex);
  const hsl = rgbToHsl(rgb.r, rgb.g, rgb.b);
  hsl.h = (hsl.h + hueShift + 360) % 360;
  const newRgb = hslToRgb(hsl.h, hsl.s, hsl.l);
  return rgbToHex(newRgb.r, newRgb.g, newRgb.b);
}

/**
 * Adjust colors for Minecraft backgrounds (always black backgrounds)
 * Ensures colors are visible on black by keeping lightness in readable range
 */
function adjustForMinecraft(hex: string, preferBrighter: boolean = false): string {
  const rgb = hexToRgb(hex);
  const hsl = rgbToHsl(rgb.r, rgb.g, rgb.b);

  // Minecraft backgrounds are always black, so we need visible colors
  // Keep lightness between 45-80% for better readability
  if (preferBrighter) {
    // Light theme: brighter colors
    if (hsl.l > 85) {
      hsl.l = 85;
    } else if (hsl.l < 50) {
      hsl.l = 60;
    }
  } else {
    // Dark theme: slightly muted but still visible
    if (hsl.l > 75) {
      hsl.l = 70;
    } else if (hsl.l < 40) {
      hsl.l = 50;
    }
  }

  const newRgb = hslToRgb(hsl.h, hsl.s, hsl.l);
  return rgbToHex(newRgb.r, newRgb.g, newRgb.b);
}

/**
 * Auto-generate a complete theme from a single base color
 */
export function generateTheme(baseColor: string, themeName: string = 'Generated Theme', preferBrighter: boolean = false): MessageTheme {
  const rgb = hexToRgb(baseColor);
  const hsl = rgbToHsl(rgb.r, rgb.g, rgb.b);

  // Generate harmonious colors based on the base color
  const primary = adjustForMinecraft(baseColor, preferBrighter);
  const secondary = adjustForMinecraft(shiftHue(baseColor, 30), preferBrighter); // Analogous
  const accent = adjustForMinecraft(shiftHue(baseColor, -30), preferBrighter); // Analogous (other direction)
  const highlight = adjustForMinecraft(adjustLightness(adjustSaturation(baseColor, 20), preferBrighter ? 20 : 15), preferBrighter);

  // Create error/success/warning with base color's saturation for harmony
  const createSemanticColor = (targetHue: number, lightness: number) => {
    const semanticRgb = hslToRgb(targetHue, hsl.s, lightness);
    return adjustForMinecraft(rgbToHex(semanticRgb.r, semanticRgb.g, semanticRgb.b), preferBrighter);
  };

  const messages: MessageTheme['messages'] = {
    // Error - Red hue with base saturation
    ERROR: {
      color: createSemanticColor(0, preferBrighter ? 65 : 60),
      decorations: ['BOLD'],
    },
    // Success - Green hue with base saturation
    SUCCESS: {
      color: createSemanticColor(120, preferBrighter ? 65 : 60),
      decorations: ['BOLD'],
    },
    // Warning - Orange hue with base saturation
    WARNING: {
      color: createSemanticColor(35, preferBrighter ? 70 : 65),
      decorations: ['BOLD'],
    },
    // Info - Use secondary color for harmony
    INFO: {
      color: secondary,
      decorations: [],
    },
    // Highlight - Brighter version of base
    HIGHLIGHT: {
      color: highlight,
      decorations: ['BOLD'],
    },
    // Primary - Base color
    PRIMARY: {
      color: primary,
      decorations: ['BOLD'],
    },
    // Secondary - Analogous color
    SECONDARY: {
      color: secondary,
      decorations: [],
    },
    // Accent - Analogous color (other direction)
    ACCENT: {
      color: accent,
      decorations: [],
    },
    // Title - High contrast, brighter in light mode
    TITLE: {
      color: preferBrighter ? '#FFFFFF' : '#E0E0E0',
      decorations: ['BOLD'],
    },
    // Subtitle - Medium emphasis
    SUBTITLE: {
      color: preferBrighter ? '#F0F0F0' : '#C0C0C0',
      decorations: [],
    },
    // Body - Normal text
    BODY: {
      color: preferBrighter ? '#FFFFFF' : '#D0D0D0',
      decorations: [],
    },
    // Label - Lower emphasis
    LABEL: {
      color: preferBrighter ? '#C0C0C0' : '#A0A0A0',
      decorations: [],
    },
    // Muted - Very low emphasis
    MUTED: {
      color: preferBrighter ? '#A0A0A0' : '#808080',
      decorations: [],
    },
    // Link - Use accent color for harmony
    LINK: {
      color: accent,
      decorations: ['UNDERLINED'],
    },
    // Disabled - Gray, strikethrough
    DISABLED: {
      color: preferBrighter ? '#808080' : '#606060',
      decorations: ['STRIKETHROUGH'],
    },
  };

  return {
    name: themeName,
    messages,
  };
}

/**
 * Get preset themes
 */
export function getPresetThemes(): Record<string, MessageTheme> {
  return {
    'minecraft-dark': {
      name: 'Minecraft Dark',
      messages: {
        ERROR: { color: '#FF5555', decorations: ['BOLD'] },
        SUCCESS: { color: '#55FF55', decorations: ['BOLD'] },
        WARNING: { color: '#FFFF55', decorations: ['BOLD'] },
        INFO: { color: '#55FFFF', decorations: [] },
        HIGHLIGHT: { color: '#FFAA00', decorations: ['BOLD'] },
        PRIMARY: { color: '#5555FF', decorations: ['BOLD'] },
        SECONDARY: { color: '#AA00AA', decorations: [] },
        MUTED: { color: '#999999', decorations: [] },
        TITLE: { color: '#FFFF55', decorations: ['BOLD'] },
        SUBTITLE: { color: '#D0D0D0', decorations: [] },
        BODY: { color: '#D0D0D0', decorations: [] },
        LABEL: { color: '#A0A0A0', decorations: [] },
        ACCENT: { color: '#FF55FF', decorations: [] },
        LINK: { color: '#55FFFF', decorations: ['UNDERLINED'] },
        DISABLED: { color: '#606060', decorations: [] },
      },
    },
    'minecraft-light': {
      name: 'Minecraft Light',
      messages: {
        ERROR: { color: '#FF8888', decorations: ['BOLD'] },
        SUCCESS: { color: '#88FF88', decorations: ['BOLD'] },
        WARNING: { color: '#FFCC66', decorations: ['BOLD'] },
        INFO: { color: '#66BBFF', decorations: [] },
        HIGHLIGHT: { color: '#FF99CC', decorations: ['BOLD'] },
        PRIMARY: { color: '#6699FF', decorations: ['BOLD'] },
        SECONDARY: { color: '#BB77DD', decorations: [] },
        MUTED: { color: '#A0A0A0', decorations: [] },
        TITLE: { color: '#FFFFFF', decorations: ['BOLD'] },
        SUBTITLE: { color: '#F0F0F0', decorations: [] },
        BODY: { color: '#FFFFFF', decorations: [] },
        LABEL: { color: '#C0C0C0', decorations: [] },
        ACCENT: { color: '#FF99BB', decorations: [] },
        LINK: { color: '#66BBFF', decorations: ['UNDERLINED'] },
        DISABLED: { color: '#808080', decorations: [] },
      },
    },
  };
}