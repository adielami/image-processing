# 🎨 Java Algorithmic Image Processor

An advanced image manipulation tool built in Java.
This application allows users to apply complex algorithms to digital images, focusing on **Pixel Sorting**, **Convolution Filters**, and low-level color channel manipulation.
The project demonstrates strong competency in algorithm optimization and 2D array handling.

---

## ⚡ Key Features

### 🌪️ Pixel Sorting (Glitch Art)
* **Algorithmic Sorting:** Sorts pixels based on brightness, hue, or saturation values.
* **Custom Thresholds:** Allows processing only specific parts of the image (e.g., "Sort only pixels brighter than 50%").
* **Visual Effect:** Creates a "melting" or "glitch" aesthetic popular in generative art.

### 🖼️ Image Filters & Kernels
* **Convolution Matrix:** Implements kernel-based processing for effects like **Blur**, **Sharpen**, and **Edge Detection**.
* **Color Manipulation:** Direct bitwise operations on RGB integers to create Grayscale, Sepia, and Inverted filters.

### 🖥️ Performance & GUI
* **Optimized Rendering:** Uses `BufferedImage` for fast pixel read/write operations.
* **Interactive UI:** Built with Java Swing/AWT to provide real-time feedback on applied effects.

---

## 🛠️ Tech Stack & Concepts

| Component | Technology | Concept Demonstrated |
|-----------|------------|----------------------|
| **Core Language** | **Java** | OOP, Strong Typing, Memory Management |
| **Graphics** | **Java AWT / Swing** | GUI Development, Event Handling |
| **Data Processing** | **2D Arrays** | Matrix manipulation, Coordinate systems |
| **Math** | **Bitwise Operators** | Extracting R,G,B values using bit shifting (`>>`, `&`) |

---

## 🔬 How It Works (The Logic)

### 1. The Pixel Sorting Algorithm
Unlike standard array sorting, sorting an image requires treating rows or columns as separate datasets.
1.  The program extracts the RGB value of each pixel (stored as a single `int`).
2.  It calculates the "luminance" (brightness) of that pixel.
3.  It applies a sorting algorithm (e.g., QuickSort or BubbleSort) to rearrange the pixels in that row based on their luminance.
4.  The result is written back to the image buffer.

### 2. Bitwise Color Extraction
To modify a pixel, the system uses bitwise operations to isolate channels:
```java
int pixel = image.getRGB(x, y);
int alpha = (pixel >> 24) & 0xff;
int red   = (pixel >> 16) & 0xff;
int green = (pixel >> 8)  & 0xff;
int blue  = (pixel)       & 0xff;
