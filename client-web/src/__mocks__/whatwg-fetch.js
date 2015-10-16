var response = {
  status: 503,
  statusText: 'Service unavailable'
};

window.fetch = () =>
  new Promise((resolve, reject) => {
    if (200 <= response.status || 300 > response.status) {
      resolve(response);
    } else {
      reject((response || response.statusText) || 'No status.');
    }
  });

export default class {
  static setResponse(value) {
    response = value;
  }
}
