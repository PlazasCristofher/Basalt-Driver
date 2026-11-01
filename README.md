# Basalt Driver

**Experimental** Android Vulkan driver plugin based on **PanVK-kbase**.

Built to work with **Panlith Launcher** (a Zalith Launcher 2 fork with PanVK loading support).
Other launchers can use it only if they implement the same load contract.

## Credit and permission

**The Vulkan driver (`libvulkan_panfrost.so`) is the work of [zenithblue-oss](https://github.com/zenithblue-oss/panvk-kbase-android).** This plugin exists thanks to it.

The driver is included **unmodified**, from release **`v26.3.1`** of [panvk-kbase-android](https://github.com/zenithblue-oss/panvk-kbase-android), and is distributed with the author's permission ([issue #24](https://github.com/zenithblue-oss/panvk-kbase-android/issues/24)). Their license texts are kept in [`licenses/panvk-kbase/`](licenses/panvk-kbase/).

## What is in the APK

| File | Role |
|------|------|
| `libvulkan_panfrost.so` | **PanVK-kbase** `v26.3.1` ICD (Mesa panvk, Mali **kbase** interface), unmodified |
| `libvulkan_panvk_shim.so` | Compatibility layer for LWJGL and Mesa/Zink (own code, Apache-2.0) |
| Manifest metadata | Registers as a driver plugin (`fclPlugin` / driver name / PanVK flag) |

### Why a shim?

The PanVK `.so` is a pure **ICD**. It exports ICD entry points (e.g. `vk_icdGetInstanceProcAddr`), **not** a full loader-style `vkGetInstanceProcAddr` the way system `libvulkan.so` does.

The shim:

1. Loads the ICD (`PANVK_ICD_PATH` or a `.so` next to the shim).
2. Exports **`vkGetInstanceProcAddr`** and **`vkGetDeviceProcAddr`** (Mesa/Zink resolve these with `dlsym` on the handle).
3. Forwards calls into the ICD.
4. Implements the Android surface path needed by the launcher stack.

An earlier shim version that did not export `vkGetDeviceProcAddr` failed in Zink; that missing export is the most likely cause, but it was not isolated on its own.

## Supported GPUs

**What the documentation says.** PanVK is the Mesa Vulkan driver for Arm Mali. According to the [Mesa Panfrost/PanVK documentation](https://docs.mesa3d.org/drivers/panfrost.html):

| Architecture | Example GPUs | Vulkan listed |
|---|---|---|
| Bifrost (v6) | G71, G72 | 1.3 |
| Bifrost (v7) | G31, G51, G52, G76 | 1.3 |
| Valhall (v9) | G57, G68 | none listed |
| Valhall (v10) | G310, G610 | 1.4 |
| Valhall (v11) | G615, G715 | 1.4 |
| 5th Gen (v12, v13, v14) | G720, G725, G1 series | 1.4 |

The same page says that on GPUs where PanVK support is experimental, the driver refuses to load by default unless `PAN_I_WANT_A_BROKEN_VULKAN_DRIVER=1` is set. It only mentions a Vulkan conformance submission for the Mali-G610.

The [PanVK-kbase project](https://github.com/zenithblue-oss/panvk-kbase-android) names a **Mali-G615 MC6** as its reference device. Its `v26.3.1` release is described by the author as a universal driver for Mali v6 to v14.

**What has been tested here.** Only a **Mali-G68 MC2** (Samsung Galaxy A16 5G, Exynos 1330, Android 16), with `PanVK-kbase RC3-lowmem` and `v26.3.1`. It works there, even though the Mesa table lists no Vulkan version for the G68, and it did not need the variable above. This is a single-device result, **not** an official support claim.

Other Mali GPUs are untested. If you try it on another device, please report the GPU model, Android version and the `latest_game.log`.

## Requirements

- A launcher with PanVK support (**Panlith** recommended)
- **Mali** GPU + working **kbase** path (devices where PanProbe can pass)
- arm64-v8a
- Modern Android (tested on 16)
- **No root**

### Reference device

Samsung Galaxy A16 5G (SM-A166M), Exynos 1330, **Mali-G68 MC2**, Android 16.
Validated with PanVK-kbase **RC3-lowmem** and **v26.3.1**, shim v4.1 (clean build).

## Install

1. Install **Panlith** (or another PanVK-aware Zalith build).
2. Install this **Basalt Driver** APK from **Releases**.
3. If the plugin does not update, uninstall the old plugin APK and install again (the signature changes; game worlds are separate).
4. In the launcher, select the driver **Basalt Driver (PanVK-kbase v26.3.1)**.
5. Start the game with native **Vulkan** (MC 26.x) or **Kopper/Zink** (OpenGL over Vulkan, tested on MC 1.8.9).

Success check: the game log shows `panvk` / `PanVK-kbase` in the `Using graphics backend Vulkan, using drivers:` line.

To use **another build or version of the driver**, replace `libvulkan_panfrost.so` inside the APK (`lib/arm64-v8a/`) with a tool such as MT Manager and sign the APK (V1 + V2 + V3).

## Packaging (rebuilders)

- `jniLibs/arm64-v8a/`:
  - `libvulkan_panvk_shim.so` (built by the workflow from `shim/`)
  - `libvulkan_panfrost.so` from [panvk-kbase-android](https://github.com/zenithblue-oss/panvk-kbase-android)
- Enable native lib extraction / legacy packaging so files exist under `nativeLibraryDir` (`extractNativeLibs=true`).
- Manifest metadata:

```xml
<meta-data android:name="fclPlugin" android:value="true" />
<meta-data android:name="driver" android:value="Basalt Driver (PanVK-kbase v26.3.1)" />
<meta-data android:name="panvkDriver" android:value="true" />
```

## Credits

PanVK-kbase (zenithblue-oss) · Mesa and Panfrost · PanProbe · PanVKDriverPlugin (muhibiscr1t1kal, Apache-2.0, base of this plugin) · Zalith Launcher 2 and PojavLauncher.

Made by one person and several AIs: ChatGPT (main ideas), Claude (code and builds), Grok (deep research) and Copilot (support).

## License

Plugin and shim code: Apache-2.0 (see `LICENSE`).

The PanVK-kbase driver is built from Mesa (MIT/X11) with PanVK-kbase's own MIT code, and includes Arm Mali kbase UAPI headers under GPL-2.0 WITH Linux-syscall-note. It keeps its own licenses; see [`licenses/panvk-kbase/`](licenses/panvk-kbase/).
