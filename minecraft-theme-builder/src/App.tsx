import { useState } from 'react';
import { MessageTheme } from './types/theme';
import { generateTheme, getPresetThemes } from './utils/themeGenerator';
import { downloadTheme, copyThemeToClipboard } from './utils/themeExporter';
import ColorPicker from './components/ColorPicker';
import MinecraftChatPreview from './components/MinecraftChatPreview';
import MinecraftItemPreview from './components/MinecraftItemPreview';

function App() {
  const [baseColor, setBaseColor] = useState('#6750A4');
  const [theme, setTheme] = useState<MessageTheme>(() => generateTheme(baseColor, 'My Custom Theme'));
  const [themeName, setThemeName] = useState('My Custom Theme');
  const [copied, setCopied] = useState(false);

  const handleGenerateTheme = (color: string) => {
    setBaseColor(color);
    setTheme(generateTheme(color, themeName));
  };

  const handlePreset = (presetName: string) => {
    const presets = getPresetThemes();
    setTheme(presets[presetName]);
    setThemeName(presets[presetName].name);
    // Update baseColor based on preset's primary color
    const primaryColor = presets[presetName].messages.PRIMARY?.color;
    if (primaryColor) {
      setBaseColor(primaryColor);
    }
  };

  const handleCopy = async () => {
    await copyThemeToClipboard(theme);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div className="min-h-screen py-8 px-4 sm:px-6 lg:px-8">
      <div className="max-w-7xl mx-auto">
        {/* Header */}
        <div className="text-center mb-8">
          <div className="inline-flex items-center justify-center p-2 bg-white/40 backdrop-blur-sm rounded-full mb-4 shadow-md">
            <span className="text-5xl">⛏</span>
          </div>
          <h1 className="text-4xl font-bold mb-2 bg-gradient-to-r from-purple-900 to-purple-600 bg-clip-text text-transparent">
            Material Theme Builder
          </h1>
          <p className="text-base text-purple-900/60">Build a Material color scheme to map dynamic color or implement a branded theme</p>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-12 gap-6">
          {/* Left Sidebar - Controls */}
          <div className="lg:col-span-4 space-y-4">
            {/* Theme Settings Card */}
            <div className="bg-white/90 backdrop-blur-sm rounded-2xl shadow-md p-6 border border-purple-100/50 relative z-10">
              <h2 className="text-lg font-semibold mb-4 text-purple-900">Source image</h2>

              <div className="space-y-4">
                <ColorPicker
                  label="Source color"
                  color={baseColor}
                  onChange={handleGenerateTheme}
                />

                <div>
                  <label className="block text-xs font-medium mb-2 text-purple-900/70">Presets</label>
                  <div className="grid grid-cols-2 gap-2">
                    <button
                      onClick={() => handlePreset('minecraft-dark')}
                      className="px-3 py-2 bg-gradient-to-br from-gray-700 to-gray-900 text-white rounded-xl hover:scale-[1.02] transform transition-all shadow-sm text-xs font-medium"
                    >
                      🌙 MC Dark
                    </button>
                    <button
                      onClick={() => handlePreset('minecraft-light')}
                      className="px-3 py-2 bg-gradient-to-br from-blue-400 to-blue-600 text-white rounded-xl hover:scale-[1.02] transform transition-all shadow-sm text-xs font-medium"
                    >
                      ☀️ MC Light
                    </button>
                  </div>
                </div>

                <div>
                  <label className="block text-xs font-medium mb-2 text-purple-900/70">Theme Name</label>
                  <input
                    type="text"
                    value={themeName}
                    onChange={(e) => {
                      setThemeName(e.target.value);
                      setTheme({ ...theme, name: e.target.value });
                    }}
                    className="w-full px-3 py-2 border border-purple-200/50 rounded-xl focus:ring-2 focus:ring-purple-400 focus:border-transparent bg-white/50 transition-all text-sm"
                  />
                </div>
              </div>
            </div>

            {/* Export Card */}
            <div className="bg-white/90 backdrop-blur-sm rounded-2xl shadow-md p-6 border border-purple-100/50 relative z-0">
              <h2 className="text-lg font-semibold mb-4 text-purple-900">Export</h2>
              <div className="space-y-2">
                <button
                  onClick={handleCopy}
                  className="w-full px-4 py-3 bg-gradient-to-r from-green-500 to-green-600 text-white rounded-xl hover:scale-[1.02] transform transition-all shadow-sm font-medium flex items-center justify-center gap-2 text-sm"
                >
                  {copied ? '✓ Copied!' : '📋 Copy JSON'}
                </button>
                <button
                  onClick={() => downloadTheme(theme)}
                  className="w-full px-4 py-3 bg-gradient-to-r from-blue-500 to-blue-600 text-white rounded-xl hover:scale-[1.02] transform transition-all shadow-sm font-medium text-sm"
                >
                  💾 Download
                </button>
              </div>
            </div>
          </div>

          {/* Right Side - Previews */}
          <div className="lg:col-span-8 space-y-4">
            {/* Chat Preview Card */}
            <div className="bg-white/90 backdrop-blur-sm rounded-2xl shadow-md p-6 border border-purple-100/50">
              <div className="flex items-center gap-2 mb-4">
                <span className="text-2xl">💬</span>
                <h2 className="text-lg font-semibold text-purple-900">Chat Preview</h2>
              </div>
              <div className="bg-gradient-to-br from-gray-900 to-gray-800 p-4 rounded-xl shadow-inner">
                <MinecraftChatPreview theme={theme} />
              </div>
            </div>

            {/* Item Lore Preview Card */}
            <div className="bg-white/90 backdrop-blur-sm rounded-2xl shadow-md p-6 border border-purple-100/50">
              <div className="flex items-center gap-2 mb-4">
                <span className="text-2xl">🗡️</span>
                <h2 className="text-lg font-semibold text-purple-900">Item Lore Preview</h2>
              </div>
              <div className="bg-gradient-to-br from-gray-900 to-gray-800 p-8 rounded-xl shadow-inner flex justify-center">
                <MinecraftItemPreview theme={theme} />
              </div>
            </div>
          </div>
        </div>

        {/* Footer */}
        <div className="mt-8 text-center text-purple-900/50 text-xs">
          <p>Made with ❤️ for Cubicolor</p>
        </div>
      </div>
    </div>
  );
}

export default App;