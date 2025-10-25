import React, { useState } from 'react';
import { HexColorPicker } from 'react-colorful';

interface Props {
  color: string;
  onChange: (color: string) => void;
  label?: string;
}

const ColorPicker: React.FC<Props> = ({ color, onChange, label }) => {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <div className="relative">
      {label && <label className="block text-xs font-medium mb-2 text-purple-900/70">{label}</label>}
      <button
        onClick={() => setIsOpen(!isOpen)}
        className="w-full h-16 rounded-xl border-2 border-purple-200/50 hover:border-purple-300 transition-colors flex items-center justify-center gap-3 bg-white/50"
      >
        <div
          className="w-10 h-10 rounded-full border-2 border-gray-300 shadow-sm"
          style={{ backgroundColor: color }}
        />
        <span className="font-mono text-sm text-purple-900">{color.toUpperCase()}</span>
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