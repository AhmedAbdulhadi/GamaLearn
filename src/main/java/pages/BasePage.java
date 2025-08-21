// src/test/java/pages/BasePage.java
package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;

import java.util.concurrent.ThreadLocalRandom;

public abstract class BasePage {
    protected final Page page;

    protected BasePage(Page page) { this.page = page; }

    // ---------- Visibility / wait ----------
    public boolean isVisible(Locator l, int timeoutMs) {
        try {
            l.waitFor(new Locator.WaitForOptions()
                    .setTimeout(timeoutMs)
                    .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void waitHidden(Locator l, int timeoutMs) {
        l.waitFor(new Locator.WaitForOptions()
                .setTimeout(timeoutMs)
                .setState(com.microsoft.playwright.options.WaitForSelectorState.HIDDEN));
    }

    // ---------- Click / Type ----------
    public void click(Locator l) {
        l.waitFor(new Locator.WaitForOptions()
                .setState(com.microsoft.playwright.options.WaitForSelectorState.ATTACHED));
        l.click();
    }

    public void type(Locator l, String text, boolean clear) {
        l.waitFor(new Locator.WaitForOptions()
                .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
        if (clear) l.fill("");
        l.fill(text);
    }

    public void clear(Locator l) {

        l.waitFor(new Locator.WaitForOptions()
                .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
        l.fill("");

    }
    // ---------- <select> helpers (native) ----------
    public void selectByLabel(Locator select, String label) {
        ready(select);
        select.selectOption(new SelectOption().setLabel(label));
    }

    public void selectByValue(Locator select, String value) {
        ready(select);
        select.selectOption(new SelectOption().setValue(value));
    }

    public void selectByIndex(Locator select, int index) {
        ready(select);
        int count = select.locator("option").count();
        if (count == 0) throw new IllegalStateException("No <option> elements found");
        int safe = Math.max(0, index) % count;
        // Avoid placeholders if possible
        safe = adjustIfPlaceholder(select, safe);
        String value = select.locator("option").nth(safe).getAttribute("value");
        select.selectOption(new SelectOption().setValue(value));
    }

    /** Select a random option. Returns the chosen label. */
    public String selectRandomOption(Locator select, boolean skipPlaceholders) {
        ready(select);
        Locator options = select.locator("option");
        int count = options.count();
        if (count == 0) throw new IllegalStateException("No <option> elements found");

        int idx = ThreadLocalRandom.current().nextInt(count); // 0..count-1
        if (skipPlaceholders) idx = adjustIfPlaceholder(select, idx);

        String value = options.nth(idx).getAttribute("value");
        String label = options.nth(idx).innerText();
        select.selectOption(new SelectOption().setValue(value));
        return label;
    }

    // ---------- internals ----------
    public void ready(Locator el) {
        el.waitFor(new Locator.WaitForOptions()
                .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
    }

    /** If option looks like a placeholder, re-roll into a non-placeholder index. */
    private int adjustIfPlaceholder(Locator select, int idx) {
        Locator opts = select.locator("option");
        int count = opts.count();
        if (count <= 1) return idx;

        String value = opts.nth(idx).getAttribute("value");
        String text  = opts.nth(idx).innerText().trim();

        if (isPlaceholder(value, text) || isDisabled(opts.nth(idx))) {
            // Prefer first non-placeholder from index 1+
            for (int i = 1; i < count; i++) {
                String v = opts.nth(i).getAttribute("value");
                String t = opts.nth(i).innerText().trim();
                if (!isPlaceholder(v, t) && !isDisabled(opts.nth(i))) return i;
            }
            // If everything looks placeholder-ish, just return original
        }
        return idx;
    }

    private boolean isPlaceholder(String value, String text) {
        if (value == null) value = "";
        String v = value.trim().toLowerCase();
        String t = (text == null ? "" : text.trim().toLowerCase());
        return v.isBlank()
                || v.equals("select") || v.equals("placeholder")
                || t.isBlank()
                || t.startsWith("select") || t.startsWith("please select");
    }

    private boolean isDisabled(Locator option) {
        String disabled = option.getAttribute("disabled");
        return disabled != null;
    }
}
