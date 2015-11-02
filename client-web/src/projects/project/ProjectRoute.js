export default {
  path: ':projectId',
  getComponent(location, cb) {
    require.ensure([], (require) => {
      cb(null, require('./ProjectView'));
    });
  }
};
