import React from 'react';
import { MessageTheme, TextStyle } from '../types/theme';

interface Props {
  theme: MessageTheme;
}

const MinecraftItemPreview: React.FC<Props> = ({ theme }) => {
  const renderText = (text: string, style?: TextStyle) => {
    if (!style) return <span>{text}</span>;

    const classes = ['minecraft-text'];
    style.decorations.forEach(dec => {
      classes.push(`text-${dec.toLowerCase()}`);
    });

    return (
      <span className={classes.join(' ')} style={{ color: style.color }}>
        {text}
      </span>
    );
  };

  return (
    <div className="minecraft-tooltip p-4 inline-block min-w-[300px]">
      <div className="space-y-1 text-sm">
        {renderText('⚔ Legendary Sword', theme.messages.TITLE)}
        <div className="h-px bg-gray-600 my-2"></div>
        {renderText('Damage: ', theme.messages.LABEL)}{renderText('+15', theme.messages.SUCCESS)}
        <br />
        {renderText('Speed: ', theme.messages.LABEL)}{renderText('Fast', theme.messages.INFO)}
        <br />
        {renderText('Durability: ', theme.messages.LABEL)}{renderText('500/500', theme.messages.PRIMARY)}
        <div className="h-px bg-gray-600 my-2"></div>
        {renderText('Special Abilities:', theme.messages.SUBTITLE)}
        <br />
        {renderText('• Critical Strike', theme.messages.HIGHLIGHT)}
        <br />
        <span>  </span>{renderText('Deal 2x damage on critical hits', theme.messages.BODY)}
        <br />
        {renderText('• Fire Aspect', theme.messages.ACCENT)}
        <br />
        <span>  </span>{renderText('Sets enemies on fire', theme.messages.BODY)}
        <div className="h-px bg-gray-600 my-2"></div>
        {renderText('Epic', theme.messages.PRIMARY)}
      </div>
    </div>
  );
};

export default MinecraftItemPreview;