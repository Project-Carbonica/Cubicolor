export type MessageRole =
  | 'ERROR'
  | 'SUCCESS'
  | 'WARNING'
  | 'INFO'
  | 'HIGHLIGHT'
  | 'PRIMARY'
  | 'SECONDARY'
  | 'MUTED'
  | 'TITLE'
  | 'SUBTITLE'
  | 'BODY'
  | 'LABEL'
  | 'ACCENT'
  | 'LINK'
  | 'DISABLED';

export type TextDecoration =
  | 'BOLD'
  | 'ITALIC'
  | 'UNDERLINED'
  | 'STRIKETHROUGH'
  | 'OBFUSCATED';

export interface TextStyle {
  color: string; // hex color
  decorations: TextDecoration[];
}

export interface MessageTheme {
  name: string;
  messages: {
    [K in MessageRole]?: TextStyle;
  };
}

export const DEFAULT_ROLES: MessageRole[] = [
  'ERROR',
  'SUCCESS',
  'WARNING',
  'INFO',
  'HIGHLIGHT',
  'TITLE',
  'BODY',
  'MUTED',
];

export const ALL_ROLES: MessageRole[] = [
  'ERROR',
  'SUCCESS',
  'WARNING',
  'INFO',
  'HIGHLIGHT',
  'PRIMARY',
  'SECONDARY',
  'MUTED',
  'TITLE',
  'SUBTITLE',
  'BODY',
  'LABEL',
  'ACCENT',
  'LINK',
  'DISABLED',
];

export const ROLE_DESCRIPTIONS: Record<MessageRole, string> = {
  ERROR: 'Error messages and critical issues',
  SUCCESS: 'Success confirmations and positive feedback',
  WARNING: 'Warning messages and cautions',
  INFO: 'Informational messages',
  HIGHLIGHT: 'Important emphasis',
  PRIMARY: 'Primary messages',
  SECONDARY: 'Secondary messages',
  MUTED: 'Subtle/less important text',
  TITLE: 'Message titles',
  SUBTITLE: 'Subtitles',
  BODY: 'Regular body text',
  LABEL: 'Labels and tags',
  ACCENT: 'Accent/emphasis',
  LINK: 'Links',
  DISABLED: 'Disabled elements',
};