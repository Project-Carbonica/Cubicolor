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
  };

  const handleCopy = async () => {
    await copyThemeToClipboard(theme);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div className="min-h-screen py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-7xl mx-auto">
        {/* Header */}
        <div className="text-center mb-12">
          <div className="inline-flex items-center justify-center p-3 bg-white/30 backdrop-blur-sm rounded-3xl mb-6 shadow-lg">
            <span className="text-6xl">⛏</span>
          </div>
          <h1 className="text-5xl font-bold mb-3 bg-gradient-to-r from-purple-900 to-purple-600 bg-clip-text text-transparent">
            Minecraft Theme Builder
          </h1>
          <p className="text-lg text-purple-900/70">Create beautiful themes for Cubicolor with Material Design</p>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-12 gap-8">
          {/* Left Sidebar - Controls */}
          <div className="lg:col-span-4 space-y-6">
            {/* Theme Settings Card */}
            <div className="bg-white/80 backdrop-blur-sm rounded-3xl shadow-lg p-8 border border-purple-100">
              <h2 className="text-xl font-semibold mb-6 text-purple-900">Theme Settings</h2>

              <div className="space-y-6">
                <div>
                  <label className="block text-sm font-medium mb-2 text-purple-900">Theme Name</label>
                  <input
                    type="text"
                    value={themeName}
                    onChange={(e) => {
                      setThemeName(e.target.value);
                      setTheme({ ...theme, name: e.target.value });
                    }}
                    className="w-full px-4 py-3 border border-purple-200 rounded-2xl focus:ring-2 focus:ring-purple-400 focus:border-transparent bg-white/50 transition-all"
                  />
                </div>

                <ColorPicker
                  label="Base Color"
                  color={baseColor}
                  onChange={handleGenerateTheme}
                />

                <div>
                  <label className="block text-sm font-medium mb-3 text-purple-900">Presets</label>
                  <div className="grid grid-cols-2 gap-3">
                    <button
                      onClick={() => handlePreset('minecraft-dark')}
                      className="px-4 py-3 bg-gradient-to-br from-gray-700 to-gray-900 text-white rounded-2xl hover:scale-105 transform transition-all shadow-md text-sm font-medium"
                    >
                      🌙 MC Dark
                    </button>
                    <button
                      onClick={() => handlePreset('minecraft-light')}
                      className="px-4 py-3 bg-gradient-to-br from-blue-400 to-blue-600 text-white rounded-2xl hover:scale-105 transform transition-all shadow-md text-sm font-medium"
                    >
                      ☀️ MC Light
                    </button>
                    <button
                      onClick={() => handlePreset('material-dark')}
                      className="px-4 py-3 bg-gradient-to-br from-purple-600 to-purple-800 text-white rounded-2xl hover:scale-105 transform transition-all shadow-md text-sm font-medium"
                    >
                      🎨 Material Dark
                    </button>
                    <button
                      onClick={() => handlePreset('material-light')}
                      className="px-4 py-3 bg-gradient-to-br from-purple-400 to-purple-600 text-white rounded-2xl hover:scale-105 transform transition-all shadow-md text-sm font-medium"
                    >
                      ✨ Material Light
                    </button>
                  </div>
                </div>
              </div>
            </div>

            {/* Export Card */}
            <div className="bg-white/80 backdrop-blur-sm rounded-3xl shadow-lg p-8 border border-purple-100">
              <h2 className="text-xl font-semibold mb-6 text-purple-900">Export</h2>
              <div className="space-y-3">
                <button
                  onClick={handleCopy}
                  className="w-full px-6 py-4 bg-gradient-to-r from-green-500 to-green-600 text-white rounded-2xl hover:scale-105 transform transition-all shadow-md font-medium flex items-center justify-center gap-2"
                >
                  {copied ? '✓ Copied to Clipboard!' : '📋 Copy JSON'}
                </button>
                <button
                  onClick={() => downloadTheme(theme)}
                  className="w-full px-6 py-4 bg-gradient-to-r from-blue-500 to-blue-600 text-white rounded-2xl hover:scale-105 transform transition-all shadow-md font-medium"
                >
                  💾 Download JSON
                </button>
              </div>
            </div>
          </div>

          {/* Right Side - Previews */}
          <div className="lg:col-span-8 space-y-6">
            {/* Chat Preview Card */}
            <div className="bg-white/80 backdrop-blur-sm rounded-3xl shadow-lg p-8 border border-purple-100">
              <div className="flex items-center gap-3 mb-6">
                <span className="text-3xl">💬</span>
                <h2 className="text-2xl font-semibold text-purple-900">Chat Preview</h2>
              </div>
              <div className="bg-gradient-to-br from-gray-900 to-gray-800 p-6 rounded-2xl shadow-inner">
                <MinecraftChatPreview theme={theme} />
              </div>
            </div>

            {/* Item Lore Preview Card */}
            <div className="bg-white/80 backdrop-blur-sm rounded-3xl shadow-lg p-8 border border-purple-100">
              <div className="flex items-center gap-3 mb-6">
                <span className="text-3xl">🗡️</span>
                <h2 className="text-2xl font-semibold text-purple-900">Item Lore Preview</h2>
              </div>
              <div className="bg-gradient-to-br from-gray-900 to-gray-800 p-10 rounded-2xl shadow-inner flex justify-center">
                <MinecraftItemPreview theme={theme} />
              </div>
            </div>
          </div>
        </div>

        {/* Footer */}
        <div className="mt-12 text-center text-purple-900/60 text-sm">
          <p>Made with ❤️ for Cubicolor | Generate themes instantly with AI-powered color algorithms</p>
        </div>
      </div>
    </div>
  );
}

export default App;