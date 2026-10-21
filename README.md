# Basalt Driver

**Experimental** Android Vulkan driver plugin based on **PanVK-kbase**.

Built to work with **Panlith Launcher** (a Zalith 2 fork with PanVK loading support).  
Other launchers can use it only if they implement the same load contract.

## What is in the APK

| File | Role |
|------|------|
| `libvulkan_panfrost.so` | **PanVK-kbase** ICD (Mesa panvk, Mali **kbase** interface) |
| `libvulkan_panvk_shim.so` | Compatibility layer for LWJGL and Mesa/Zink |
| Manifest metadata | Registers as a driver plugin (`fclPlugin` / driver name / PanVK flag) |

### Why a shim?

The PanVK `.so` is a pure **ICD**. It exports ICD entry points (e.g. `vk_icdGetInstanceProcAddr`), **not** a full loader-style `vkGetInstanceProcAddr` the way system `libvulkan.so` does.

The shim:

1. Loads the ICD (`PANVK_ICD_PATH` or a `.so` next to the shim).
2. Exports **`vkGetInstanceProcAddr`** and **`vkGetDeviceProcAddr`** (Mesa/Zink resolve these with `dlsym` on the handle).
3. Forwards calls into the ICD.
4. Supports the Android surface path needed by the launcher stack.

Without the shim, Zink often dies before device selection (e.g. missing proc addresses).

## Requirements

- A launcher with PanVK support (**Panlith** recommended)
- **Mali** GPU + working **kbase** path (devices where PanProbe can pass)
- arm64-v8a
- Modern Android (tested on 16)
- **No root**

### Reference device

Samsung Galaxy A16 5G (SM-A166M), Exynos 1330, **Mali-G68 MC2**, Android 16.  
Validated with PanVK-kbase **RC3** and shim v4 (GIPA + GDPA).

## Install

1. Install **Panlith** (or another PanVK-aware Zalith build).
2. Install this **Basalt-DriverPlugin** APK.
3. If the plugin does not update, uninstall the old plugin APK and install again (game worlds are separate).
4. In the launcher, select driver **Basalt / PanVK-kbase**.
5. Start the game with native **Vulkan** (MC 26.x) or **Zink/Kopper/Ironized** (OpenGL-over-Vulkan).

Success check: F3 or game log shows `panvk` / `PanVK-kbase` / `MESA_PANVK`.


<img width="2340" height="1080" alt="323049" src="https://github.com/user-attachments/assets/3059e46d-311e-4bab-bb20-aad0e0cc9380" />
<img width="2340" height="1080" alt="323097" src="https://github.com/user-attachments/assets/3b7ee582-d341-4bb8-b127-359929c1f591" />
<img width="2340" height="1080" alt="323589" src="https://github.com/user-attachments/assets/05fbc820-1b50-4485-a392-b54ce2c54c79" />

## Packaging (rebuilders)

- `jniLibs/arm64-v8a/`:
  - `libvulkan_panvk_shim.so`
  - `libvulkan_panfrost.so` from [panvk-kbase-android](https://github.com/zenithblue-oss/panvk-kbase-android)
- Enable native lib extraction / legacy packaging so files exist under `nativeLibraryDir`.
- Example metadata:

```xml
<meta-data android:name="fclPlugin" android:value="true" />
<meta-data android:name="driver" android:value="PanVK-kbase RC3" />
<meta-data android:name="panvkDriver" android:value="true" />

