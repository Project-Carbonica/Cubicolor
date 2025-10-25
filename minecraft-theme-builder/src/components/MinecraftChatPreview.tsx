import React from 'react';
import { MessageTheme, TextStyle } from '../types/theme';

interface Props {
  theme: MessageTheme;
}

const MinecraftChatPreview: React.FC<Props> = ({ theme }) => {
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
    <div className="minecraft-chat space-y-1 text-sm">
      <div>{renderText('✗ Error: ', theme.messages.ERROR)}{renderText('Connection lost!', theme.messages.BODY)}</div>
      <div>{renderText('✓ Success: ', theme.messages.SUCCESS)}{renderText('Theme saved successfully', theme.messages.BODY)}</div>
      <div>{renderText('⚠ Warning: ', theme.messages.WARNING)}{renderText('Low health detected', theme.messages.BODY)}</div>
      <div>{renderText('ℹ Info: ', theme.messages.INFO)}{renderText('Press F to open menu', theme.messages.BODY)}</div>
      <div>{renderText('[Server] ', theme.messages.HIGHLIGHT)}{renderText('Player joined the game', theme.messages.BODY)}</div>
      <div>{renderText('<Player> ', theme.messages.PRIMARY)}{renderText('Hello world!', theme.messages.BODY)}</div>
      <div>{renderText('Tip: ', theme.messages.ACCENT)}{renderText('Use /help for commands ', theme.messages.MUTED)}</div>
    </div>
  );
};

export default MinecraftChatPreview;