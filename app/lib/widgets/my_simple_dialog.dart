import 'package:flutter/material.dart';

class MySimpleDialog extends StatelessWidget {
  const MySimpleDialog({super.key, required this.title, required this.child});

  final Widget title;
  final Widget child;

  @override
  Widget build(BuildContext context) {
    // Scaffold: 스낵바를 다이아로그 위에 띄우기 위해서
    // TODO: 근데 이렇게 하면 dismiss 제대로 안됨
    return Scaffold(
      backgroundColor: Colors.transparent,
      body: SimpleDialog(
        title: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          mainAxisSize: MainAxisSize.min,
          children: [
            title,
            const CloseButton(),
          ],
        ),
        children: [SimpleDialogOption(child: child)],
      ),
    );
  }
}
