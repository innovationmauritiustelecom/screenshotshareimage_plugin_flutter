import 'dart:async';

import 'package:flutter/services.dart';

class ScreenshotShareImage {
  static const MethodChannel _channel =
      const MethodChannel('screenshot_share_image');

  static Future<Null> takeScreenshotShareImage() async {
    Map<String, dynamic> args = <String, dynamic>{};
    await _channel.invokeMethod('takeScreenshotAndShare', args);
    return null;
  }
}
