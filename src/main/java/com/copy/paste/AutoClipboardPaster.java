package com.copy.paste;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class AutoClipboardPaster implements NativeKeyListener {

    private List<String> strings;
    private int currentIndex = 1;  // we preload index 0
    private final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    private boolean firstPasteDone = false;

    public AutoClipboardPaster(List<String> strings) {
        this.strings = strings;
    }

    public static void main(String[] args) {
        // Change this path to your local file location
       // String filePath = "C:\\Users\\YourName\\Documents\\strings.txt";
    	
    	String filePath = "/home/kalyan/Documents/strings.txt";


        // Disable JNativeHook’s verbose logging
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);

        List<String> strings;
        try {
            strings = Files.readAllLines(Path.of(filePath))
                    .stream()
                    .filter(line -> !line.trim().isEmpty()) // skip empty lines
                    .toList();
        } catch (IOException e) {
            System.err.println("❌ Failed to read strings file: " + e.getMessage());
            return;
        }

        if (strings.isEmpty()) {
            System.err.println("❌ The file is empty. Please add some lines of text.");
            return;
        }

        AutoClipboardPaster listener = new AutoClipboardPaster(strings);

        try {
            GlobalScreen.registerNativeHook();
        } catch (Exception e) {
            System.err.println("❌ Failed to register native hook: " + e.getMessage());
            return;
        }

        GlobalScreen.addNativeKeyListener(listener);

        // ✅ Preload the first string immediately
        listener.copyToClipboard(strings.get(0));
        System.out.println("✅ Auto Clipboard Paster started!");
        System.out.println("➡ Loaded strings from: " + filePath);
        System.out.println("➡ First string copied: \"" + strings.get(0) + "\"");
        System.out.println("Press CTRL + V to paste it. Each next CTRL + V loads the next string.");
        System.out.println("Press ESC to exit.");
    }

    private void copyToClipboard(String text) {
        clipboard.setContents(new StringSelection(text), null);
        System.out.println("Loaded to clipboard: " + text);
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        // Exit program on ESC
        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            try {
                GlobalScreen.unregisterNativeHook();
                System.out.println("👋 Program exited.");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return;
        }

        // Detect CTRL + V
        if (e.getKeyCode() == NativeKeyEvent.VC_V &&
                (e.getModifiers() & NativeKeyEvent.CTRL_MASK) != 0) {

            // First Ctrl+V just pastes the first preloaded string
            if (!firstPasteDone) {
                firstPasteDone = true;
                return;
            }

            // Then load the next string
            if (currentIndex < strings.size()) {
                copyToClipboard(strings.get(currentIndex++));
            } else {
                System.out.println("✅ All strings have been used!");
            }
        }
    }

    @Override public void nativeKeyReleased(NativeKeyEvent e) {}
    @Override public void nativeKeyTyped(NativeKeyEvent e) {}
}
