# Basalt Driver

Plugin de driver Vulkan para **Panlith Launcher** (fork no oficial de Zalith Launcher 2).
Empaqueta el ICD PanVK-kbase y un shim que expone los simbolos Vulkan que piden Mesa/Zink y la superficie Android.

> Experimental. Probado solo en Samsung Galaxy A16 5G (Mali-G68 MC2, Android 16).

## Contenido

- `libvulkan_panfrost.so`: ICD PanVK-kbase (binario de terceros, se agrega a mano; ver licencia abajo).
- `libvulkan_panvk_shim.so`: se compila en el workflow desde `shim/` (build limpia).

## Compilar

1. Pon `libvulkan_panfrost.so` en `app/src/main/jniLibs/arm64-v8a/`.
2. Sube el repo: el workflow compila el shim y el APK. El APK sale en Artifacts.

## Creditos

PanVK-kbase (zenithblue-oss), Mesa y Panfrost, PanProbe, PanVKDriverPlugin (muhibiscr1t1kal, Apache-2.0, base de este plugin), Zalith Launcher 2 y PojavLauncher.

## Licencia

Codigo propio del plugin y del shim: Apache-2.0 (ver `LICENSE`). El binario PanVK-kbase conserva sus licencias originales (Mesa MIT/X11 y otras); su redistribucion esta pendiente de confirmar, por eso este repo debe mantenerse privado hasta entonces.
