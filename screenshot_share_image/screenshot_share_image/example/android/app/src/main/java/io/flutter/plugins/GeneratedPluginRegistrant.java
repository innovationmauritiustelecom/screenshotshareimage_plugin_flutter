package io.flutter.plugins;

import io.flutter.plugin.common.PluginRegistry;
import mt.innovation.screenshotshareimage.ScreenshotShareImagePlugin;

/**
 * Generated file. Do not edit.
 */
public final class GeneratedPluginRegistrant {
  public static void registerWith(PluginRegistry registry) {
    if (alreadyRegisteredWith(registry)) {
      return;
    }
    ScreenshotShareImagePlugin.registerWith(registry.registrarFor("mt.innovation.screenshotshareimage.ScreenshotShareImagePlugin"));
  }

  private static boolean alreadyRegisteredWith(PluginRegistry registry) {
    final String key = GeneratedPluginRegistrant.class.getCanonicalName();
    if (registry.hasPlugin(key)) {
      return true;
    }
    registry.registrarFor(key);
    return false;
  }
}
