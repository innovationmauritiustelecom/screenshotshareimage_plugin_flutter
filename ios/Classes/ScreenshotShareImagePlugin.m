#import "ScreenshotShareImagePlugin.h"

@implementation ScreenshotShareImagePlugin

FlutterViewController *controller = nil;
UIWindow *keyWindow = nil;

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"screenshot_share_image"
            binaryMessenger:[registrar messenger]];

  //controller = (UIViewController*)[UIApplication sharedApplication].keyWindow.rootViewController;
  keyWindow = [[[UIApplication sharedApplication] delegate] window];
  controller = (FlutterViewController*)keyWindow.rootViewController;

  ScreenshotShareImagePlugin* instance = [[ScreenshotShareImagePlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
  if ([@"takeScreenshotAndShare" isEqualToString:call.method]) {
    [self takeScreenshotAndShare: controller.view];
  } else {
    result(FlutterMethodNotImplemented);
  }
}

- (void)takeScreenshotAndShare:(UIView*)view {
    
    CGFloat scale = [UIScreen mainScreen].scale;
    
    UIGraphicsBeginImageContextWithOptions(view.bounds.size, view.opaque, 0.0f);
    
    [view drawViewHierarchyInRect:view.bounds afterScreenUpdates:YES];
    UIImage *img = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    NSArray *items = @[img];
    
    UIActivityViewController *avc = [[UIActivityViewController alloc]initWithActivityItems:items applicationActivities:nil];
    [controller presentViewController:avc animated: YES completion:nil];

}

@end
