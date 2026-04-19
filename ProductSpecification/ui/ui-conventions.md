# UI Conventions

## Design Tokens

### Colors

| Token | Value | Usage |
|-------|-------|-------|
| `--bg-primary` | `#f8f9fa` | Page background |
| `--bg-white` | `#ffffff` | Cards, modals |
| `--bg-column` | `#f1f3f5` | Column backgrounds |
| `--text-primary` | `#212529` | Headings, body text |
| `--text-secondary` | `#6c757d` | Muted text, labels |
| `--text-placeholder` | `#adb5bd` | Placeholder text |
| `--border` | `#dee2e6` | Borders, dividers |
| `--border-focus` | `#4dabf7` | Focused input border |
| `--accent` | `#228be6` | Primary buttons, links |
| `--accent-hover` | `#1c7ed6` | Button hover state |
| `--accent-light` | `#e7f5ff` | Accent backgrounds |
| `--danger` | `#fa5252` | Error text, error borders |
| `--danger-bg` | `#fff5f5` | Error message backgrounds |
| `--success` | `#40c057` | Success indicators |
| `--column-todo` | `#e7f5ff` | To Do column header accent |
| `--column-progress` | `#fff9db` | In Progress column header accent |
| `--column-done` | `#ebfbee` | Done column header accent |

### Typography

| Element | Font | Size | Weight | Color |
|---------|------|------|--------|-------|
| App title | Inter | 20px | 700 | `--text-primary` |
| Column header | Inter | 14px | 600 | `--text-primary` |
| Task card title | Inter | 14px | 500 | `--text-primary` |
| Task card description | Inter | 13px | 400 | `--text-secondary` |
| Form label | Inter | 14px | 500 | `--text-primary` |
| Form input | Inter | 14px | 400 | `--text-primary` |
| Button text | Inter | 14px | 500 | `#ffffff` |
| Error message | Inter | 13px | 400 | `--danger` |
| Empty state text | Inter | 14px | 400 | `--text-secondary` |
| Counter badge | Inter | 12px | 600 | `--text-secondary` |

### Spacing

| Token | Value |
|-------|-------|
| `--space-xs` | 4px |
| `--space-sm` | 8px |
| `--space-md` | 12px |
| `--space-lg` | 16px |
| `--space-xl` | 24px |
| `--space-2xl` | 32px |

### Border Radius

| Element | Radius |
|---------|--------|
| Cards | 8px |
| Buttons | 6px |
| Inputs | 6px |
| Columns | 12px |
| Modals | 12px |

### Shadows

| Element | Shadow |
|---------|--------|
| Card | `0 1px 3px rgba(0,0,0,0.08)` |
| Card hover | `0 2px 8px rgba(0,0,0,0.12)` |
| Modal overlay | `rgba(0,0,0,0.5)` backdrop |
| Modal | `0 8px 32px rgba(0,0,0,0.16)` |

## Components

### Task Card

- White background, 8px radius, card shadow
- Padding: 12px
- Title: 14px/500, single line (truncate with ellipsis if needed)
- Description preview: 13px/400, max 2 lines, `--text-secondary`
- Hover: elevated shadow

### Column

- Background: `--bg-column`
- Border radius: 12px
- Header: column name + task count badge
- Column header has colored accent strip (4px top border matching column color)
- Min height: fills viewport minus header
- Tasks stack vertically with 8px gap

### Board Header

- White background, bottom border
- App title left-aligned
- "Add Task" button right-aligned
- Height: 56px
- Horizontal padding: 24px

### Form / Modal

- Centered modal with overlay backdrop
- 12px border radius, white background
- Padding: 24px
- Form fields stack vertically with 16px gap
- Labels above inputs
- Input height: 40px (text input), 120px (textarea)
- Input border: 1px solid `--border`, focus: `--border-focus` with 3px ring
- Character counter below constrained fields: right-aligned, `--text-secondary`, 12px

### Error States

- Inline field errors: red text below the field, 13px
- Error input border: `--danger`
- Banner errors (duplicate title): `--danger-bg` background, `--danger` text, x-circle icon

### Buttons

- Primary: `--accent` bg, white text, 6px radius, 40px height, 16px horizontal padding
- Primary hover: `--accent-hover`
- Secondary/Cancel: transparent bg, `--text-secondary` text, border
- Danger: `--danger` bg, white text, 6px radius, 40px height (desktop), 44px (mobile)
- Danger hover: `#e03131`
- Disabled: 50% opacity, no pointer events

### Icon Buttons (Card Actions)

- Delete button: `trash-2` icon, `--text-placeholder` color by default
- Hover: `--danger` color, `--danger-bg` background, 4px radius
- Positioned in card header, flex-shrink: 0
- Mobile: min touch target 44x44px with 8px padding

### Confirmation Dialog

- Desktop: centered modal, 400px width, 12px radius, modal shadow
- Mobile: bottom sheet (anchored to bottom), full width, 12px top radius
- Warning icon: `alert-triangle` in circular `--danger-bg` container (48px)
- Title: 18px/600, centered
- Body text: 14px/400, `--text-secondary`, centered, includes task name in bold
- Actions: desktop = side-by-side (Cancel + Danger), mobile = stacked (Danger first, Cancel second)
- Overlay: `rgba(0,0,0,0.5)` backdrop

### Highlighted Card

- Active/targeted card: 2px solid `--danger` outline, -2px offset
- Used during destructive action confirmation to indicate which card is affected

### Empty State

- Centered text in column body
- Clipboard icon + "No tasks yet" message
- `--text-secondary` color

## Layout

### Desktop (1400px)

- Board: 3 equal columns with 16px gap
- Columns fill available height (viewport - header)
- Board horizontal padding: 24px
- Task creation: modal overlay

### Mobile (375px)

- Board: single column view, horizontal swipe or tab bar to switch columns
- Active column takes full width
- Column tabs at top (To Do | In Progress | Done)
- Task creation: full-screen form (slides up)
- Bottom padding for safe area
