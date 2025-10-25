import React, { useState } from 'react';
import { HexColorPicker } from 'react-colorful';

interface Props {
  color: string;
  onChange: (color: string) => void;
  label?: string;
  baseColor?: string;
}

const ColorPicker: React.FC<Props> = ({ color, onChange, label, baseColor = '#6750A4' }) => {
  const [isOpen, setIsOpen] = useState(false);

  // Convert hex to RGB for dynamic theming
  const hexToRgb = (hex: string) => {
    const result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
    return result ? {
      r: parseInt(result[1], 16),
      g: parseInt(result[2], 16),
      b: parseInt(result[3], 16)
    } : { r: 103, g: 80, b: 164 };
  };

  const rgb = hexToRgb(baseColor);

  return (
    <div className="relative">
      {label && <label className="block text-xs font-medium mb-2" style={{ color: `rgba(${rgb.r}, ${rgb.g}, ${rgb.b}, 0.7)` }}>{label}</label>}
      <button
        onClick={() => setIsOpen(!isOpen)}
        className="w-full h-16 rounded-xl border-2 hover:border-purple-300 transition-colors flex items-center justify-center gap-3 bg-white/50"
        style={{
          borderColor: `rgba(${rgb.r}, ${rgb.g}, ${rgb.b}, 0.3)`
        }}
      >
        <div
          className="w-10 h-10 rounded-full border-2 border-gray-300 shadow-sm"
          style={{ backgroundColor: color }}
        />
        <span className="font-mono text-sm" style={{ color: baseColor }}>{color.toUpperCase()}</span>
      </button>
      {isOpen && (
        <>
          <div
            className="fixed inset-0 z-[100]"
            onClick={() => setIsOpen(false)}
          />
          <div className="absolute z-[101] mt-2 p-4 bg-white rounded-xl shadow-2xl border border-purple-200">
            <HexColorPicker color={color} onChange={onChange} />
          </div>
        </>
      )}
    </div>
  );
};

export default ColorPicker;