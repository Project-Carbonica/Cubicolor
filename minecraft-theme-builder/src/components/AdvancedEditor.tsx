import React, { useState } from 'react';
import { MessageTheme, MessageRole, TextDecoration, ALL_ROLES, ROLE_DESCRIPTIONS } from '../types/theme';
import ColorPicker from './ColorPicker';

interface Props {
  theme: MessageTheme;
  onThemeChange: (theme: MessageTheme) => void;
  defaultOpen?: boolean;
}

const AdvancedEditor: React.FC<Props> = ({ theme, onThemeChange, defaultOpen = false }) => {
  const [isOpen, setIsOpen] = useState(defaultOpen);
  const [expandedRoles, setExpandedRoles] = useState<Set<MessageRole>>(new Set());

  const toggleRole = (role: MessageRole) => {
    setExpandedRoles(prev => {
      const newSet = new Set(prev);
      if (newSet.has(role)) {
        newSet.delete(role);
      } else {
        newSet.add(role);
      }
      return newSet;
    });
  };

  const expandAll = () => {
    const allRoles = ALL_ROLES.filter(role => theme.messages[role]);
    setExpandedRoles(new Set(allRoles));
  };

  const collapseAll = () => {
    setExpandedRoles(new Set());
  };

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
        <>
          <div className="px-6 pt-2 pb-3 border-b border-purple-100/50">
            <div className="flex gap-2">
              <button
                onClick={expandAll}
                className="px-3 py-1.5 text-xs bg-purple-100 text-purple-700 rounded-lg hover:bg-purple-200 transition-colors font-medium"
              >
                Expand All
              </button>
              <button
                onClick={collapseAll}
                className="px-3 py-1.5 text-xs bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition-colors font-medium"
              >
                Collapse All
              </button>
            </div>
          </div>
          <div className="px-6 pb-6 space-y-2 max-h-[600px] overflow-y-auto">
            {ALL_ROLES.map(role => {
            const style = theme.messages[role];
            if (!style) return null;
            const isExpanded = expandedRoles.has(role);

            return (
              <div key={role} className="border border-purple-100 rounded-xl bg-white/50 overflow-hidden">
                <button
                  onClick={() => toggleRole(role)}
                  className="w-full px-4 py-3 flex items-center justify-between text-left hover:bg-purple-50/50 transition-colors"
                >
                  <div className="flex items-center gap-3 flex-1">
                    <div
                      className="w-6 h-6 rounded-full border-2 border-gray-300 shadow-sm flex-shrink-0"
                      style={{ backgroundColor: style.color }}
                    />
                    <div className="flex-1 min-w-0">
                      <h3 className="text-sm font-semibold text-purple-900">{role}</h3>
                      <p className="text-xs text-purple-600/60 truncate">{ROLE_DESCRIPTIONS[role]}</p>
                    </div>
                  </div>
                  <span className={`text-lg transform transition-transform flex-shrink-0 ml-2 ${isExpanded ? 'rotate-180' : ''}`}>
                    ▼
                  </span>
                </button>

                {isExpanded && (
                  <div className="px-4 pb-4 space-y-3 border-t border-purple-100/50 pt-3">
                    <ColorPicker
                      label="Color"
                      color={style.color}
                      onChange={(color) => handleColorChange(role, color)}
                    />

                    <div>
                      <label className="block text-xs font-medium mb-2 text-purple-900/70">Decorations</label>
                      <div className="flex flex-wrap gap-2">
                        {(['BOLD', 'ITALIC', 'UNDERLINED', 'STRIKETHROUGH', 'OBFUSCATED'] as TextDecoration[]).map(decoration => (
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
                )}
              </div>
            );
          })}
          </div>
        </>
      )}
    </div>
  );
};

export default AdvancedEditor;
