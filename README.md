# 🧩 Auto Clipboard Paster (Java)

A simple **Java automation tool** that pastes multiple predefined strings one after another — anywhere on your system — by just pressing **Ctrl + V**.  
It automatically updates your clipboard with the next string every time you paste.

---

## 🚀 Features

- ✅ Paste multiple strings one by one with **Ctrl + V**
- ✅ Reads all strings from a **local text file** (each line = one string)
- ✅ Works system-wide using **global keyboard listener**
- ✅ Compatible with **Windows, macOS, and Linux (Kali tested)**
- ✅ Stops safely with **ESC**

---

## 🧠 How It Works

1. The program reads all strings from a local file (each string on a new line).
2. It automatically loads the **first string** into the system clipboard.
3. Every time you press **Ctrl + V** anywhere on your computer:
   - The current clipboard content is pasted.
   - The program automatically loads the **next** string.
4. When you reach the end of the list, the program stops (or can be modified to loop).
5. Press **ESC** to exit the tool anytime.

---

## 🧩 File Example

Create a file with one string per line, e.g.  
`/home/kalyan/strings.txt`

```text
youtube
facebook
twitter
instagram
linkedin
