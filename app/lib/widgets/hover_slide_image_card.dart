import 'dart:async';

import 'package:flutter/material.dart';

class HoverSlideImageCard extends StatefulWidget {
  const HoverSlideImageCard({
    Key? key,
    required this.onTap,
    required this.image,
    required this.overlayWidget,
  }) : super(key: key);

  final GestureTapCallback onTap;
  final ImageProvider image;
  final Widget overlayWidget;

  @override
  State<HoverSlideImageCard> createState() => _HoverSlideImageCardState();
}

class _HoverSlideImageCardState extends State<HoverSlideImageCard>
    with SingleTickerProviderStateMixin {
  late final AnimationController _controller = AnimationController(
    duration: const Duration(milliseconds: 200),
    vsync: this,
  );

  late final Animation<Offset> _offsetAnimation = Tween<Offset>(
    begin: Offset.zero,
    end: const Offset(0.0, -0.07),
  ).animate(CurvedAnimation(
    parent: _controller,
    curve: Curves.easeOutSine,
  ));

  late final Animation<double> _opacityAnimation = Tween<double>(
    begin: 1.0,
    end: 0.1, // 희미하게 보인다
  ).animate(CurvedAnimation(
    parent: _controller,
    curve: Curves.easeOutSine,
  ));

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  void _handleHover(bool hovered) {
    if (hovered) {
      _controller.forward();
    } else {
      _controller.reverse();
    }
  }

  Future<void> _handleTap() async {
    await _controller.reverse();
    widget.onTap();
  }

  @override
  Widget build(BuildContext context) {
    return _TransparentInkWell(
      onTap: _handleTap,
      onHover: _handleHover,
      child: SlideTransition(
        position: _offsetAnimation,
        child: _Card(
          child: Stack(
            alignment: Alignment.bottomLeft,
            children: [
              _Image(
                opacity: _opacityAnimation,
                image: widget.image,
              ),
              FadeTransition(
                opacity: _controller.view,
                child: widget.overlayWidget,
              ),
              // description
            ],
          ),
        ),
      ),
    );
  }
}

class _TransparentInkWell extends StatelessWidget {
  const _TransparentInkWell({this.onTap, this.onHover, this.child});

  final GestureTapCallback? onTap;
  final ValueChanged<bool>? onHover;
  final Widget? child;

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: onTap,
      onHover: onHover,
      hoverColor: Colors.transparent,
      highlightColor: Colors.transparent,
      splashColor: Colors.transparent,
      focusColor: Colors.transparent,
      child: child,
    );
  }
}

class _Card extends StatelessWidget {
  const _Card({required this.child});

  final Widget child;

  @override
  Widget build(BuildContext context) {
    final themeData = Theme.of(context);
    final backgroundColor = themeData.brightness == Brightness.dark
        ? Colors.black
        : themeData.colorScheme.surface;
    return Theme(
      data: themeData.copyWith(
        colorScheme: themeData.colorScheme.copyWith(surface: backgroundColor),
      ),
      child: Card(
        clipBehavior: Clip.antiAlias,
        margin: EdgeInsets.zero,
        child: child,
      ),
    );
  }
}

class _Image extends StatelessWidget {
  const _Image({required this.opacity, required this.image});

  final ImageProvider image;
  final Animation<double> opacity;

  @override
  Widget build(BuildContext context) {
    return Image(
      image: image,
      opacity: opacity,
      fit: BoxFit.cover,
    );
  }
}
