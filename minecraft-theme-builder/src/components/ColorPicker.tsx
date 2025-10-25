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
      {label && <label className="block text-sm font-medium mb-1">{label}</label>}
      <button
        onClick={() => setIsOpen(!isOpen)}
        className="w-full h-10 rounded border-2 border-gray-300 hover:border-gray-400 transition-colors flex items-center px-3 gap-2 bg-white"
      >
        <div
          className="w-6 h-6 rounded border border-gray-400"
          style={{ backgroundColor: color }}
        />
        <span className="font-mono text-sm">{color.toUpperCase()}</span>
      </button>
      {isOpen && (
        <>
          <div
            className="fixed inset-0 z-10"
            onClick={() => setIsOpen(false)}
          />
          <div className="absolute z-20 mt-2 p-3 bg-white rounded-lg shadow-xl border border-gray-200">
            <HexColorPicker color={color} onChange={onChange} />
          </div>
        </>
      )}
    </div>
  );
};

export default ColorPicker;