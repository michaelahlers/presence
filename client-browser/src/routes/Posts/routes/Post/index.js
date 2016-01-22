const route = {
  path: ':postId',

  getComponent(location, cb) {
    require.ensure([], (require) => {
      cb(null, require('./components/Post.js'));
    });
  }
};

module.exports = route;
