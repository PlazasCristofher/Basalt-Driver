# Basalt Driver

An **experimental** Vulkan driver plugin based on **PanVK-kbase** for Android.

Intended for use with **Panlith Launcher** (a fork of Zalith 2 with PanVK support).

It may also be of interest to other launchers that implement the same loading contract.

## What's Included in the APK

| File | Function |

|---------|---------|

`libvulkan_panfrost.so` | **PanVK-kbase** ICD (PanVK Mesa, kbase/Mali interface) |

`libvulkan_panvk_shim.so` | Compatibility layer for LWJGL and Mesa/Zink |

`AndroidManifest` metadata | Registered as a driver plugin (`fclPlugin` / driver / PanVK brand) |

### Why the shim

The PanVK ICD **doesn't** export `vkGetInstanceProcAddr` as a system `libvulkan.so` file. It exports the ICD API (`vk_icdGetInstanceProcAddr`, etc.).

The solution:

1. Load the ICD (`PANVK_ICD_PATH` or the `.so` file along with the shim).

2. Export **`vkGetInstanceProcAddr`** and **`vkGetDeviceProcAddr`** (Zink/Mesa request these with `dlsym` on the handle).

3. Forward calls to the ICD.

4. Participate in the Android surface when needed for the launcher stack.

Without the shim, Zink usually fails before reaching "Vulkan device selected" (`ZINK: could not get process address` or equivalent).

##Requirements

- Launcher with PanVK path (recommended: **Panlith**)
- **Mali** GPU + accessible **kbase** kernel/userspace (as on devices where PanProbe passes tests)
- arm64-v8a
- Recent Android version (tested on 16)
- **Sin Root**

### Reference Device

Samsung Galaxy A16 5G (SM-A166M), Exynos 1330, **Mali-G68 MC2**, Android 16.
PanProbe/in-game usage: PanVK-kbase **RC3** (and load testing with shim v4).

## Installation

1. Install **Panlith** (or a Zalith with the PanVK patch).

2. Install this APK (**Basalt**).

3. If Android doesn't update the plugin, uninstall the previous version and reinstall it (do not delete game worlds).

4. In the launcher: **Basalt / PanVK-kbase** driver.

5. Start the game with:

- **Native Vulkan** (MC 26.x), or

- **Zink / Kopper / Ironized** (OpenGL over Vulkan).

Check by pressing F3 or in the game log for something like: `panvk`, `PanVK-kbase`, `MESA_PANVK`.

## Packaging (for those who need to recompile the plugin)

- `jniLibs/arm64-v8a/`:

- `libvulkan_panvk_shim.so`

- `libvulkan_panfrost.so` (ICD of [panvk-kbase-android](https://github.com/zenithblue-oss/panvk-kbase-android))

- `extractNativeLibs` / `useLegacyPackaging` according to the plugin template, so that real files exist under `nativeLibraryDir`.

- Example metadata:

```xml
<metadata android:name="fclPlugin" android:value="true" />
<metadata android:name="controller" android:value="PanVK-kbase RC3" />
<metadata android:name="panvkDriver" android:value="true" />

Many thanks to zenithblue-oss for the Vulkan library; without it, this wouldn't have been possible.

 
