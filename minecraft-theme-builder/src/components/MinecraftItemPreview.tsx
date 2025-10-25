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
        {renderText('Kanal 1', theme.messages.TITLE)}
        <div className="h-px bg-gray-600 my-2"></div>
        {renderText('Bu kanala giriş yapabilmek için ', theme.messages.BODY)}{renderText('sol tıklayınız', theme.messages.LINK)}
        <br />
        {renderText('Bu kanaldan çıkış yapabilmek için ', theme.messages.BODY)}{renderText('sağ tıklayınız', theme.messages.LINK)}
        <div className="h-px bg-gray-600 my-2"></div>
        {renderText('Ayrıca bu kanalı görmek için ', theme.messages.MUTED)}{renderText('VIP', theme.messages.HIGHLIGHT)}{renderText(' olmalısınız.', theme.messages.MUTED)}
        <div className="h-px bg-gray-600 my-2"></div>
        {renderText('Kanal Bilgileri:', theme.messages.SUBTITLE)}
        <br />
        {renderText('Aktif Oyuncular: ', theme.messages.LABEL)}{renderText('23', theme.messages.SUCCESS)}
        <br />
        {renderText('Durum: ', theme.messages.LABEL)}{renderText('Açık', theme.messages.INFO)}
        <br />
        {renderText('Tip: ', theme.messages.LABEL)}{renderText('Genel Sohbet', theme.messages.ACCENT)}
      </div>
    </div>
  );
};

export default MinecraftItemPreview;