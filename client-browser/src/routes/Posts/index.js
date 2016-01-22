const route = {
  path: 'posts',

  getComponent(location, cb) {
    require.ensure([], (require) => {
      cb(null, require('./components/Posts.js'));
    });
  },

  getChildRoutes(location, cb) {
    require.ensure([], (require) => {
      cb(null, [
        require('./routes/Post')
      ]);
    });
  }
};

module.exports = route;
