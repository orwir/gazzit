import 'dart:convert';
import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:redux/redux.dart';
import 'package:redux_thunk/redux_thunk.dart';
import 'package:starrit/model/feed.dart';
import 'package:starrit/model/post.dart';
import 'package:starrit/model/state.dart';
import 'package:starrit/service/feed.dart';

@immutable
class PostsLoadingStartAction {
  final Feed feed;

  PostsLoadingStartAction(this.feed);

  @override
  String toString() => '{$runtimeType: {feed: $feed}}';
}

@immutable
class PostsLoadingSuccessAction {
  final Feed feed;
  final List<Post> posts;

  PostsLoadingSuccessAction(this.feed, this.posts);

  @override
  String toString() => '{$runtimeType: {feed: $feed, posts: ${posts.length}}}';
}

@immutable
class PostsLoadingFailureAction {
  final Feed feed;
  final Exception error;

  PostsLoadingFailureAction(this.feed, this.error);

  @override
  String toString() => '{$runtimeType: {feed: $feed, error: "$error"}}';
}

ThunkAction<AppState> loadPosts(Feed feed, String after) {
  return (Store<AppState> store) async {
    store.dispatch(PostsLoadingStartAction(feed));
    final response = await listing(
      domain: 'reddit.com',
      feed: feed,
      after: after,
    );
    if (response.statusCode == 200) {
      final result = jsonDecode(response.body);
      final posts = (result['data']['children'] as List<dynamic>)
          .map((json) => Post.fromJson(json['data']))
          .toList();
      store.dispatch(PostsLoadingSuccessAction(feed, posts));
    } else {
      final error =
          HttpException('request failed with code: ${response.statusCode}');
      store.dispatch(PostsLoadingFailureAction(feed, error));
    }
  };
}
