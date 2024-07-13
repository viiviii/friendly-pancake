import 'package:flutter/material.dart';

typedef WidgetBuilder<T> = Widget Function(BuildContext context, T data);

class MyFutureBuilder<T> extends StatelessWidget {
  const MyFutureBuilder({
    super.key,
    required this.future,
    required this.builder,
  });

  final Future<T>? future;
  final WidgetBuilder<T> builder;

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<T>(
      future: future,
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.none) {
          return const SizedBox.shrink();
        } else if (snapshot.hasData) {
          return builder(context, snapshot.data as T);
        } else if (snapshot.hasError) {
          return ErrorMessage(message: '${snapshot.error}');
        } else {
          return const _ProgressIndicator();
        }
      },
    );
  }
}

class ErrorMessage<T> extends StatelessWidget {
  const ErrorMessage({super.key, required this.message});

  final String message;

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const Icon(
            Icons.error_outline,
            color: Colors.red,
            size: 60,
          ),
          Padding(
            padding: const EdgeInsets.only(top: 16),
            child: Text(
              message,
              maxLines: 5,
              softWrap: true,
              overflow: TextOverflow.ellipsis,
            ),
          ),
        ],
      ),
    );
  }
}

class _ProgressIndicator extends StatelessWidget {
  const _ProgressIndicator();

  @override
  Widget build(BuildContext context) {
    return const Center(
      child: SizedBox(
        width: 60,
        height: 60,
        child: CircularProgressIndicator(),
      ),
    );
  }
}
