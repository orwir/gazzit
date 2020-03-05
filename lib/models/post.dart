import 'package:flutter/foundation.dart';
import 'package:starrit/utils/json.dart';
import 'package:starrit/utils/object.dart';
import 'subreddit.dart';
import 'author.dart';
import 'image.dart';

enum PostType { image, gif, video, text, link }

@immutable
class Post {
  Post({
    @required this.id,
    @required this.subreddit,
    @required this.author,
    @required this.created,
    @required this.title,
    @required this.nsfw,
    @required this.spoiler,
    @required this.comments,
    @required this.score,
    @required this.domain,
    @required this.selfDomain,
    @required this.postUrl,
    @required this.contentUrl,
    @required this.type,
    @required this.imagePreview,
    @required this.imageSource,
    @required this.imageBlurred,
    @required this.text,
    @required this.gif,
    @required this.video,
  });

  Post.fromJson(Map<String, dynamic> json)
      : this(
          id: json['name'],
          subreddit: Subreddit.fromJson(json),
          author: Author.fromJson(json),
          created: json.created,
          title: json['title'],
          nsfw: json['over_18'] ?? false,
          spoiler: json['spoiler'] ?? false,
          comments: json['num_comments'],
          score: json['score'],
          domain: json.domain,
          selfDomain: json.selfDomain,
          postUrl: json['permalink'],
          contentUrl: json.url,
          type: json.type,
          imagePreview: json.image('preview.images[0].resolutions[0]'),
          imageSource: json.image('preview.images[0].source'),
          imageBlurred: json.image(
            'preview.images[0].variants.nsfw.resolutions[0]',
          ),
          text: json.text,
          gif: json.gif,
          video: json.video,
        );

  final String id;
  final Subreddit subreddit;
  final Author author;
  final DateTime created;
  final String title;
  final bool nsfw;
  final bool spoiler;
  final int comments;
  final int score;
  final String domain;
  final bool selfDomain;
  final String postUrl;
  final String contentUrl;
  final PostType type;
  final PostImage imagePreview;
  final PostImage imageSource;
  final PostImage imageBlurred;
  final String text;
  final String gif;
  final String video;

  @override
  int get hashCode => hash([
        id,
        subreddit,
        author,
        created,
        title,
        nsfw,
        spoiler,
        comments,
        domain,
        postUrl,
        contentUrl,
        type,
        imagePreview,
        imageSource,
        imageBlurred,
        text,
        gif,
        video,
      ]);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        other is Post &&
            runtimeType == other.runtimeType &&
            id == other.id &&
            subreddit == other.subreddit &&
            author == other.author &&
            created == other.created &&
            title == other.title &&
            nsfw == other.nsfw &&
            spoiler == other.spoiler &&
            comments == other.comments &&
            domain == other.domain &&
            postUrl == other.postUrl &&
            contentUrl == other.contentUrl &&
            type == other.type &&
            imagePreview == other.imagePreview &&
            imageSource == other.imageSource &&
            imageBlurred == other.imageBlurred &&
            text == other.text &&
            gif == other.gif &&
            video == other.video;
  }
}

extension _ on Map<String, dynamic> {
  DateTime get created => DateTime.fromMillisecondsSinceEpoch(
        get<double>('created_utc').toInt() * 1000,
        isUtc: true,
      );

  String get domain => string('domain');

  bool get selfDomain => domain.startsWith('self.');

  String get url => string('url');

  PostType get type {
    if (isImage) return PostType.image;
    if (video != null) return PostType.video;
    if (gif != null) return PostType.gif;
    if (text != null || selfDomain) return PostType.text;
    return PostType.link;
  }

  PostImage image(String key) {
    return get<Object>(key)?.into((json) => PostImage.fromJson(json));
  }

  String get text => string('selftext') ?? string('selftext_html');

  String get gif => url.endsWith('.gif') ? url : null;

  String get video {
    if (domain == 'i.imgur.com' && url.endsWith('.gifv')) {
      return url.replaceAll('http://', 'https://').replaceAll('.gifv', '.mp4');
    }
    if (domain == 'v.redd.it') {
      final key = 'secure_media.reddit_video.hls_url';
      return string(key) ?? string('crosspost_parent_list[0].$key');
    }
    if (domain == 'gfycat.com') {
      final key = 'secure_media.oembed.thumbnail_url';
      final url = string(key) ?? string('crosspost_parent_list[0].$key');
      if (url != null) {
        return url
            .replaceAll('thumbs.gfycat.com', 'giant.gfycat.com')
            .replaceAll('-size_restricted', 'replace')
            .replaceAll('.gif', '.mp4');
      }
    }
    return null;
  }

  bool get isImage {
    final lastSegment = Uri.parse(string('url')).pathSegments.last;
    final ext = lastSegment?.substring(lastSegment.lastIndexOf('.') + 1);
    return _imageExtensions.contains(ext?.toLowerCase());
  }
}

const _imageExtensions = [
  'bmp',
  'jpg',
  'jpeg',
  'png',
  'svg',
  'tif',
  'tiff',
  'jfif',
  'pjpeg',
  'pjp',
  'ico',
  'cur',
];
