import React, { useState } from 'react';
import { MessageTheme, MessageRole, TextDecoration, ALL_ROLES, ROLE_DESCRIPTIONS } from '../types/theme';
import ColorPicker from './ColorPicker';

interface Props {
  theme: MessageTheme;
  onThemeChange: (theme: MessageTheme) => void;
}

const AdvancedEditor: React.FC<Props> = ({ theme, onThemeChange }) => {
  const [isOpen, setIsOpen] = useState(false);

  const handleColorChange = (role: MessageRole, color: string) => {
    const updatedTheme = {
      ...theme,
      messages: {
        ...theme.messages,
        [role]: {
          ...theme.messages[role]!,
          color,
        },
      },
    };
    onThemeChange(updatedTheme);
  };

  const handleDecorationToggle = (role: MessageRole, decoration: TextDecoration) => {
    const currentStyle = theme.messages[role];
    if (!currentStyle) return;

    const decorations = currentStyle.decorations.includes(decoration)
      ? currentStyle.decorations.filter(d => d !== decoration)
      : [...currentStyle.decorations, decoration];

    const updatedTheme = {
      ...theme,
      messages: {
        ...theme.messages,
        [role]: {
          ...currentStyle,
          decorations,
        },
      },
    };
    onThemeChange(updatedTheme);
  };

  return (
    <div className="bg-white/90 backdrop-blur-sm rounded-2xl shadow-md border border-purple-100/50">
      <button
        onClick={() => setIsOpen(!isOpen)}
        className="w-full px-6 py-4 flex items-center justify-between text-left hover:bg-purple-50/50 transition-colors rounded-2xl"
      >
        <div>
          <h2 className="text-lg font-semibold text-purple-900">Advanced Color Editor</h2>
          <p className="text-xs text-purple-600/70 mt-1">Customize individual color roles</p>
        </div>
        <span className={`text-2xl transform transition-transform ${isOpen ? 'rotate-180' : ''}`}>
          ▼
        </span>
      </button>

      {isOpen && (
        <div className="px-6 pb-6 space-y-4 max-h-[600px] overflow-y-auto">
          {ALL_ROLES.map(role => {
            const style = theme.messages[role];
            if (!style) return null;

            return (
              <div key={role} className="border border-purple-100 rounded-xl p-4 bg-white/50">
                <div className="flex items-center justify-between mb-3">
                  <div>
                    <h3 className="text-sm font-semibold text-purple-900">{role}</h3>
                    <p className="text-xs text-purple-600/60">{ROLE_DESCRIPTIONS[role]}</p>
                  </div>
                  <div
                    className="w-8 h-8 rounded-full border-2 border-gray-300 shadow-sm"
                    style={{ backgroundColor: style.color }}
                  />
                </div>

                <div className="grid grid-cols-1 gap-3">
                  <ColorPicker
                    label="Color"
                    color={style.color}
                    onChange={(color) => handleColorChange(role, color)}
                  />

                  <div>
                    <label className="block text-xs font-medium mb-2 text-purple-900/70">Decorations</label>
                    <div className="flex flex-wrap gap-2">
                      {(['BOLD', 'ITALIC', 'UNDERLINED', 'STRIKETHROUGH'] as TextDecoration[]).map(decoration => (
                        <button
                          key={decoration}
                          onClick={() => handleDecorationToggle(role, decoration)}
                          className={`px-3 py-1 text-xs rounded-lg transition-all ${
                            style.decorations.includes(decoration)
                              ? 'bg-purple-600 text-white shadow-sm'
                              : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
                          }`}
                        >
                          {decoration.charAt(0) + decoration.slice(1).toLowerCase()}
                        </button>
                      ))}
                    </div>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
};

export default AdvancedEditor;
