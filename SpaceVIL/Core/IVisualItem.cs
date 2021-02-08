using System;
using System.Collections.Generic;
using System.Drawing;
using SpaceVIL.Decorations;

namespace SpaceVIL.Core
{
    /// <summary>
    /// Not implemented!!!
    /// </summary>
    public interface Interface
    {
        String GetToolTip();

        void SetToolTip(String text);

        Spacing GetSpacing();

        void SetSpacing(Spacing spacing);

        void SetSpacing(int horizontal, int vertical);

        Indents GetPadding();

        void SetPadding(Indents padding);

        void SetPadding(int left, int top, int right, int bottom);

        void SetBorder(Border border);

        void SetBorderFill(Color fill);

        Color GetBorderFill();

        void SetBorderFill(int r, int g, int b);

        void SetBorderFill(int r, int g, int b, int a);

        void SetBorderFill(float r, float g, float b);

        void SetBorderFill(float r, float g, float b, float a);

        void SetBorderRadius(CornerRadius radius);

        CornerRadius GetBorderRadius();

        void SetBorderThickness(int thickness);

        int GetBorderThickness();

        void SetBackground(int r, int g, int b);

        void SetBackground(int r, int g, int b, int a);

        void SetBackground(float r, float g, float b);

        void SetBackground(float r, float g, float b, float a);

        void SetMinSize(int width, int height);

        int[] GetMinSize();

        void SetMaxSize(int width, int height);

        int[] GetMaxSize();

        void SetAlignment(List<ItemAlignment> alignment);

        void SetSizePolicy(SizePolicy width, SizePolicy height);

        void SetShadowExtension(int wExtension, int hExtension);

        void AddItemState(ItemStateType type, ItemState state);

        void RemoveItemState(ItemStateType type);

        void RemoveAllItemStates();

        ItemState GetState(ItemStateType type);

        void UpdateState();

        void InsertItem(IBaseItem item, int index);

        void AddItem(IBaseItem item);

        bool IsPassEvents();

        bool IsPassEvents(InputEventType e);

        List<InputEventType> GetPassEvents();

        List<InputEventType> GetNonPassEvents();

        void SetPassEvents(bool value);

        void SetPassEvents(bool value, InputEventType e);

        void SetPassEvents(bool value, List<InputEventType> events_Set);

        void SetPassEvents(bool value, params InputEventType[] events_Set);

        bool IsDisabled();

        void SetDisabled(bool value);

        bool IsMouseHover();

        void SetMouseHover(bool value);

        bool IsMousePressed();

        void SetMousePressed(bool value);

        bool IsFocused();

        void SetFocused(bool value);

        bool GetHoverVerification(float xpos, float ypos);

        List<IBaseItem> GetItems();

        bool RemoveItem(IBaseItem item);

        void Clear();

        void AddEventListener(GeometryEventType type, IBaseItem listener);

        void RemoveEventListener(GeometryEventType type, IBaseItem listener);

        ItemStateType GetCurrentStateType();

        void SetState(ItemStateType state);

        void SetContent(List<IBaseItem> content);

        Figure IsCustomFigure();

        void SetCustomFigure(Figure figure);
    }
}