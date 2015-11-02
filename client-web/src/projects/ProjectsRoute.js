export default {
  path: 'projects',
  getComponent(location, cb) {
    require.ensure([], (require) => {
      cb(null, require('./ProjectsView'));
    });
  },
  getChildRoutes(location, cb) {
    require.ensure([], (require) => {
      cb(null, [
        require('./project')
      ]);
    });
  }
};
