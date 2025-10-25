import { MessageTheme } from '../types/theme';

/**
 * Export theme to Cubicolor JSON format
 */
export function exportTheme(theme: MessageTheme): string {
  return JSON.stringify(theme, null, 2);
}

/**
 * Import theme from JSON string
 */
export function importTheme(jsonString: string): MessageTheme {
  try {
    const parsed = JSON.parse(jsonString);

    // Validate structure
    if (!parsed.name || typeof parsed.name !== 'string') {
      throw new Error('Invalid theme: missing or invalid name');
    }

    if (!parsed.messages || typeof parsed.messages !== 'object') {
      throw new Error('Invalid theme: missing or invalid messages');
    }

    return parsed as MessageTheme;
  } catch (error) {
    throw new Error(`Failed to import theme: ${error instanceof Error ? error.message : 'Unknown error'}`);
  }
}

/**
 * Download theme as JSON file
 */
export function downloadTheme(theme: MessageTheme): void {
  const json = exportTheme(theme);
  const blob = new Blob([json], { type: 'application/json' });
  const url = URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.href = url;
  link.download = `${theme.name.toLowerCase().replace(/\s+/g, '-')}.json`;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  URL.revokeObjectURL(url);
}

/**
 * Copy theme JSON to clipboard
 */
export async function copyThemeToClipboard(theme: MessageTheme): Promise<void> {
  const json = exportTheme(theme);
  await navigator.clipboard.writeText(json);
}

/**
 * Download both dark and light theme versions
 */
export function downloadBothThemes(darkTheme: MessageTheme, lightTheme: MessageTheme): void {
  // Download dark theme
  const darkJson = exportTheme(darkTheme);
  const darkBlob = new Blob([darkJson], { type: 'application/json' });
  const darkUrl = URL.createObjectURL(darkBlob);
  const darkLink = document.createElement('a');
  darkLink.href = darkUrl;
  const baseName = darkTheme.name.toLowerCase().replace(/\s+/g, '-').replace(/-?\(?(dark|light)\)?-?/gi, '');
  darkLink.download = `${baseName}-dark.json`;
  document.body.appendChild(darkLink);
  darkLink.click();
  document.body.removeChild(darkLink);
  URL.revokeObjectURL(darkUrl);

  // Download light theme after a short delay
  setTimeout(() => {
    const lightJson = exportTheme(lightTheme);
    const lightBlob = new Blob([lightJson], { type: 'application/json' });
    const lightUrl = URL.createObjectURL(lightBlob);
    const lightLink = document.createElement('a');
    lightLink.href = lightUrl;
    lightLink.download = `${baseName}-light.json`;
    document.body.appendChild(lightLink);
    lightLink.click();
    document.body.removeChild(lightLink);
    URL.revokeObjectURL(lightUrl);
  }, 100);
}