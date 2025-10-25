import { useState, useEffect } from 'react';
import { MessageTheme } from './types/theme';
import { generateTheme, getPresetThemes } from './utils/themeGenerator';
import { downloadTheme, copyThemeToClipboard, downloadBothThemes } from './utils/themeExporter';
import ColorPicker from './components/ColorPicker';
import MinecraftChatPreview from './components/MinecraftChatPreview';
import MinecraftItemPreview from './components/MinecraftItemPreview';
import AdvancedEditor from './components/AdvancedEditor';

type Step = 1 | 2 | 3 | 4;

function App() {
  const [currentStep, setCurrentStep] = useState<Step>(1);
  const [useAdvancedSettings, setUseAdvancedSettings] = useState(false);
  const [baseColor, setBaseColor] = useState('#6750A4');
  const [preferBrighter, setPreferBrighter] = useState(false);
  const [theme, setTheme] = useState<MessageTheme>(() => generateTheme(baseColor, 'My Custom Theme', false));
  const [themeName, setThemeName] = useState('My Custom Theme');
  const [copied, setCopied] = useState(false);

  const handleGenerateTheme = (color: string) => {
    setBaseColor(color);
    setTheme(generateTheme(color, themeName, preferBrighter));
  };

  const handleBrightnessMode = (mode: 'dark' | 'light') => {
    const isBright = mode === 'light';
    setPreferBrighter(isBright);
    setTheme(generateTheme(baseColor, themeName, isBright));
    setThemeName(`${themeName.replace(' (Dark)', '').replace(' (Light)', '')} (${mode === 'dark' ? 'Dark' : 'Light'})`);
  };

  const handleCopy = async () => {
    await copyThemeToClipboard(theme);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  const handleThemeChange = (updatedTheme: MessageTheme) => {
    setTheme(updatedTheme);
  };

  const handleDownloadBoth = () => {
    // Generate both dark and light themes
    const baseNameClean = themeName.replace(/\s*\(?(Dark|Light)\)?$/i, '').trim();
    const darkTheme = generateTheme(baseColor, `${baseNameClean} (Dark)`, false);
    const lightTheme = generateTheme(baseColor, `${baseNameClean} (Light)`, true);

    // If user customized with advanced settings, apply those customizations to both
    if (useAdvancedSettings) {
      // Use current theme as is (with user's customizations)
      const otherTheme = preferBrighter
        ? generateTheme(baseColor, `${baseNameClean} (Dark)`, false)
        : generateTheme(baseColor, `${baseNameClean} (Light)`, true);

      downloadBothThemes(
        preferBrighter ? otherTheme : theme,
        preferBrighter ? theme : otherTheme
      );
    } else {
      downloadBothThemes(darkTheme, lightTheme);
    }
  };

  const handleNext = () => {
    if (currentStep === 1) {
      setCurrentStep(2);
    } else if (currentStep === 2 && useAdvancedSettings) {
      setCurrentStep(3);
    } else if (currentStep === 3) {
      setCurrentStep(4);
    }
  };

  const handleBack = () => {
    if (currentStep === 4) {
      setCurrentStep(useAdvancedSettings ? 3 : 2);
    } else if (currentStep === 3) {
      setCurrentStep(2);
    } else if (currentStep === 2) {
      setCurrentStep(1);
    }
  };

  const handleSkipAdvanced = () => {
    setUseAdvancedSettings(false);
    setCurrentStep(4); // Go to export step
  };

  const handleUseAdvanced = () => {
    setUseAdvancedSettings(true);
    setCurrentStep(3);
  };

  const totalSteps = 4;

  // Apply theme to website
  useEffect(() => {
    // Convert hex to RGB for better manipulation
    const hexToRgb = (hex: string) => {
      const result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
      return result ? {
        r: parseInt(result[1], 16),
        g: parseInt(result[2], 16),
        b: parseInt(result[3], 16)
      } : { r: 103, g: 80, b: 164 };
    };

    const rgb = hexToRgb(baseColor);

    // Apply CSS variables to root
    document.documentElement.style.setProperty('--theme-primary-r', rgb.r.toString());
    document.documentElement.style.setProperty('--theme-primary-g', rgb.g.toString());
    document.documentElement.style.setProperty('--theme-primary-b', rgb.b.toString());
    document.documentElement.style.setProperty('--theme-primary', baseColor);
  }, [baseColor]);

  return (
    <div
      className="min-h-screen py-8 px-4 sm:px-6 lg:px-8"
      style={{
        background: `linear-gradient(to bottom, rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.03), rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.08))`
      }}
    >
      <div className="max-w-7xl mx-auto">
        {/* Header */}
        <div className="text-center mb-8">
          <h1
            className="text-4xl font-bold mb-2 bg-clip-text text-transparent"
            style={{
              backgroundImage: `linear-gradient(to right, ${baseColor}, rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.6))`
            }}
          >
            Material Theme Builder
          </h1>
          <p className="text-base" style={{ color: `rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.6)` }}>
            Build a Material color scheme to map dynamic color or implement a branded theme
          </p>
        </div>

        {/* Step Indicator */}
        <div className="mb-6 flex items-center justify-center gap-2">
          {[1, 2, 3, 4].map((step) => {
            const isActive = step === currentStep;
            const isCompleted = step < currentStep;
            const isSkipped = step === 3 && !useAdvancedSettings && currentStep >= 4;

            return (
              <div key={step} className="flex items-center">
                <div
                  className={`w-10 h-10 rounded-full flex items-center justify-center text-sm font-semibold transition-all ${
                    isActive
                      ? 'text-white ring-4 scale-110'
                      : isCompleted
                      ? 'bg-green-500 text-white'
                      : isSkipped
                      ? 'bg-gray-300 text-gray-500 line-through'
                      : 'bg-gray-200 text-gray-500'
                  }`}
                  style={isActive ? {
                    backgroundColor: baseColor,
                    boxShadow: `0 0 0 4px rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.2)`
                  } : {}}
                >
                  {step}
                </div>
                {step < 4 && (
                  <div
                    className={`w-16 h-1 mx-1 transition-all ${
                      step < currentStep || isSkipped
                        ? 'bg-green-500'
                        : 'bg-gray-200'
                    }`}
                  />
                )}
              </div>
            );
          })}
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-12 gap-6">
          {/* Left Sidebar - Controls */}
          <div className="lg:col-span-4 space-y-4">
            {/* Step 1: Color Selection */}
            {currentStep === 1 && (
              <div
                className="backdrop-blur-sm rounded-2xl shadow-md p-6 relative z-10"
                style={{
                  backgroundColor: `rgba(255, 255, 255, 0.9)`,
                  borderWidth: '1px',
                  borderStyle: 'solid',
                  borderColor: `rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.15)`,
                  background: `linear-gradient(135deg, rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.05), rgba(255, 255, 255, 0.95))`
                }}
              >
                <h2 className="text-lg font-semibold mb-4" style={{ color: baseColor }}>Step 1: Choose Your Colors</h2>

                <div className="space-y-4">
                  <ColorPicker
                    label="Source color"
                    color={baseColor}
                    onChange={handleGenerateTheme}
                    baseColor={baseColor}
                  />

                  <div>
                    <label className="block text-xs font-medium mb-2" style={{ color: `rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.7)` }}>Brightness Mode</label>
                    <div className="grid grid-cols-2 gap-2">
                      <button
                        onClick={() => handleBrightnessMode('dark')}
                        className={`px-3 py-2 bg-gradient-to-br from-gray-700 to-gray-900 text-white rounded-xl hover:scale-[1.02] transform transition-all shadow-sm text-xs font-medium ${!preferBrighter ? 'ring-2 ring-purple-500' : ''}`}
                      >
                        Dark
                      </button>
                      <button
                        onClick={() => handleBrightnessMode('light')}
                        className={`px-3 py-2 bg-gradient-to-br from-blue-400 to-blue-600 text-white rounded-xl hover:scale-[1.02] transform transition-all shadow-sm text-xs font-medium ${preferBrighter ? 'ring-2 ring-purple-500' : ''}`}
                      >
                        Light
                      </button>
                    </div>
                  </div>

                  <div>
                    <label className="block text-xs font-medium mb-2" style={{ color: `rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.7)` }}>Theme Name</label>
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

                  <button
                    onClick={handleNext}
                    className="w-full px-4 py-3 text-white rounded-xl hover:scale-[1.02] transform transition-all shadow-sm font-medium text-sm mt-4"
                    style={{
                      backgroundImage: `linear-gradient(to right, ${baseColor}, rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.8))`
                    }}
                  >
                    Next
                  </button>
                </div>
              </div>
            )}

            {/* Step 2: Advanced Settings Prompt */}
            {currentStep === 2 && (
              <div
                className="backdrop-blur-sm rounded-2xl shadow-md p-6 relative z-10"
                style={{
                  backgroundColor: `rgba(255, 255, 255, 0.9)`,
                  borderWidth: '1px',
                  borderStyle: 'solid',
                  borderColor: `rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.15)`,
                  background: `linear-gradient(135deg, rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.05), rgba(255, 255, 255, 0.95))`
                }}
              >
                <h2 className="text-lg font-semibold mb-4" style={{ color: baseColor }}>Step 2: Advanced Settings</h2>
                <p className="text-sm mb-6" style={{ color: `rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.7)` }}>
                  Would you like to customize individual color roles and decorations?
                </p>

                <div className="space-y-3">
                  <button
                    onClick={handleUseAdvanced}
                    className="w-full px-4 py-3 text-white rounded-xl hover:scale-[1.02] transform transition-all shadow-sm font-medium text-sm"
                    style={{
                      backgroundImage: `linear-gradient(to right, ${baseColor}, rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.8))`
                    }}
                  >
                    Yes, customize advanced settings
                  </button>
                  <button
                    onClick={handleSkipAdvanced}
                    className="w-full px-4 py-3 bg-gradient-to-r from-gray-400 to-gray-500 text-white rounded-xl hover:scale-[1.02] transform transition-all shadow-sm font-medium text-sm"
                  >
                    No, use default settings
                  </button>
                  <button
                    onClick={handleBack}
                    className="w-full px-4 py-3 bg-white border-2 border-purple-300 text-purple-700 rounded-xl hover:bg-purple-50 transform transition-all shadow-sm font-medium text-sm"
                  >
                    Back
                  </button>
                </div>
              </div>
            )}

            {/* Step 3: Advanced Editor (shown if user chose advanced) */}
            {currentStep === 3 && (
              <div
                className="backdrop-blur-sm rounded-2xl shadow-md p-6 relative z-10"
                style={{
                  backgroundColor: `rgba(255, 255, 255, 0.9)`,
                  borderWidth: '1px',
                  borderStyle: 'solid',
                  borderColor: `rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.15)`,
                  background: `linear-gradient(135deg, rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.05), rgba(255, 255, 255, 0.95))`
                }}
              >
                <h2 className="text-lg font-semibold mb-4" style={{ color: baseColor }}>Step 3: Fine-tune Colors</h2>
                <p className="text-sm mb-4" style={{ color: `rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.7)` }}>
                  Customize each color role individually
                </p>
                <div className="space-y-2">
                  <button
                    onClick={handleNext}
                    className="w-full px-4 py-3 text-white rounded-xl hover:scale-[1.02] transform transition-all shadow-sm font-medium text-sm"
                    style={{
                      backgroundImage: `linear-gradient(to right, ${baseColor}, rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.8))`
                    }}
                  >
                    Next
                  </button>
                  <button
                    onClick={handleBack}
                    className="w-full px-4 py-3 bg-white rounded-xl transform transition-all shadow-sm font-medium text-sm"
                    style={{
                      borderWidth: '2px',
                      borderColor: `rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.3)`,
                      color: baseColor
                    }}
                  >
                    Back
                  </button>
                </div>
              </div>
            )}

            {/* Step 4: Export & Summary */}
            {currentStep === 4 && (
              <div
                className="backdrop-blur-sm rounded-2xl shadow-md p-6 relative z-10"
                style={{
                  backgroundColor: `rgba(255, 255, 255, 0.9)`,
                  borderWidth: '1px',
                  borderStyle: 'solid',
                  borderColor: `rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.15)`,
                  background: `linear-gradient(135deg, rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.05), rgba(255, 255, 255, 0.95))`
                }}
              >
                <h2 className="text-lg font-semibold mb-4" style={{ color: baseColor }}>Step 4: Export Theme</h2>

                {/* Theme Summary */}
                <div className="mb-6 space-y-3">
                  <div
                    className="rounded-xl p-4"
                    style={{
                      backgroundColor: `rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.08)`
                    }}
                  >
                    <h3 className="text-sm font-semibold mb-2" style={{ color: baseColor }}>Theme Summary</h3>
                    <div className="space-y-2 text-sm">
                      <div className="flex justify-between">
                        <span style={{ color: `rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.7)` }}>Name:</span>
                        <span className="font-medium" style={{ color: baseColor }}>{theme.name}</span>
                      </div>
                      <div className="flex justify-between">
                        <span style={{ color: `rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.7)` }}>Base Color:</span>
                        <div className="flex items-center gap-2">
                          <div className="w-4 h-4 rounded border border-gray-300" style={{ backgroundColor: baseColor }} />
                          <span className="font-medium" style={{ color: baseColor }}>{baseColor}</span>
                        </div>
                      </div>
                      <div className="flex justify-between">
                        <span style={{ color: `rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.7)` }}>Mode:</span>
                        <span className="font-medium" style={{ color: baseColor }}>{preferBrighter ? 'Light' : 'Dark'}</span>
                      </div>
                      <div className="flex justify-between">
                        <span style={{ color: `rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.7)` }}>Advanced:</span>
                        <span className="font-medium" style={{ color: baseColor }}>{useAdvancedSettings ? 'Yes' : 'No'}</span>
                      </div>
                      <div className="flex justify-between">
                        <span style={{ color: `rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.7)` }}>Color Roles:</span>
                        <span className="font-medium" style={{ color: baseColor }}>{Object.keys(theme.messages).length}</span>
                      </div>
                    </div>
                  </div>
                </div>

                {/* Export Buttons */}
                <div className="space-y-2">
                  <button
                    onClick={handleCopy}
                    className="w-full px-4 py-3 bg-gradient-to-r from-green-500 to-green-600 text-white rounded-xl hover:scale-[1.02] transform transition-all shadow-sm font-medium text-sm"
                  >
                    {copied ? 'Copied!' : 'Copy JSON'}
                  </button>
                  <button
                    onClick={handleDownloadBoth}
                    className="w-full px-4 py-3 bg-gradient-to-r from-blue-500 to-blue-600 text-white rounded-xl hover:scale-[1.02] transform transition-all shadow-sm font-medium text-sm"
                  >
                    Download Dark & Light
                  </button>
                  <button
                    onClick={handleBack}
                    className="w-full px-4 py-3 bg-white border-2 border-purple-300 text-purple-700 rounded-xl hover:bg-purple-50 transform transition-all shadow-sm font-medium text-sm"
                  >
                    Back
                  </button>
                </div>
              </div>
            )}
          </div>

          {/* Right Side - Advanced Editor and Previews */}
          <div className="lg:col-span-8 flex flex-col lg:flex-row gap-4">
            {/* Advanced Editor Column - Only show in Step 3 */}
            {currentStep === 3 && (
              <div className="flex-1 lg:order-1 order-2">
                <AdvancedEditor theme={theme} onThemeChange={handleThemeChange} defaultOpen={true} />
              </div>
            )}

            {/* Previews Column - Sticky on desktop */}
            <div className={`space-y-4 ${currentStep === 3 ? 'lg:w-80 w-full flex-shrink-0 lg:order-2 order-1' : 'flex-1'}`}>
              <div className="lg:sticky lg:top-4 space-y-4">
                {/* Chat Preview Card */}
                <div
                  className="backdrop-blur-sm rounded-2xl shadow-md p-6"
                  style={{
                    backgroundColor: `rgba(255, 255, 255, 0.9)`,
                    borderWidth: '1px',
                    borderStyle: 'solid',
                    borderColor: `rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.15)`,
                    background: `linear-gradient(135deg, rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.05), rgba(255, 255, 255, 0.95))`
                  }}
                >
                  <div className="flex items-center gap-2 mb-4">
                    <h2 className="text-lg font-semibold" style={{ color: baseColor }}>Chat Preview</h2>
                  </div>
                  <div className="bg-gradient-to-br from-gray-900 to-gray-800 p-4 rounded-xl shadow-inner">
                    <MinecraftChatPreview theme={theme} />
                  </div>
                </div>

                {/* Item Lore Preview Card */}
                <div
                  className="backdrop-blur-sm rounded-2xl shadow-md p-6"
                  style={{
                    backgroundColor: `rgba(255, 255, 255, 0.9)`,
                    borderWidth: '1px',
                    borderStyle: 'solid',
                    borderColor: `rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.15)`,
                    background: `linear-gradient(135deg, rgba(var(--theme-primary-r), var(--theme-primary-g), var(--theme-primary-b), 0.05), rgba(255, 255, 255, 0.95))`
                  }}
                >
                  <div className="flex items-center gap-2 mb-4">
                    <h2 className="text-lg font-semibold" style={{ color: baseColor }}>Item Lore Preview</h2>
                  </div>
                  <div className="bg-gradient-to-br from-gray-900 to-gray-800 p-8 rounded-xl shadow-inner flex justify-center">
                    <MinecraftItemPreview theme={theme} />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Footer */}
        <div className="mt-8 text-center text-purple-900/50 text-xs">
          <p>Made for Cubicolor</p>
        </div>
      </div>
    </div>
  );
}

export default App;