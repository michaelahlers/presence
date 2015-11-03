# Michael Ahlers Presence Web Client

## Prerequisites

### OS X

1. Follow installation instructions for [Homebrew](http://brew.sh/).
1. `$ brew install awscli`
  - If deploying to S3.
1. `$ brew install node`
1. `$ npm install`
  - Whenever dependencies are changed.

## Development

1. `$ npm run develop`
  - Will watch source trees for changes.
  - Browser will automatically refresh the web application.
  - Visit `http://localhost:8080/webpack-dev-server/` to run.

## Deployment

### S3

Assuming a bucket called “my-client-web-bucket”:

1. Create a bucket with:
  - Bucket policy under _Permissions_ containing:

    ```json
    {
      "Statement": [
        {
          "Sid": "PublicReadForGetBucketObjects",
          "Effect": "Allow",
          "Principal": "*",
          "Action": "s3:GetObject",
          "Resource": "arn:aws:s3:::my-client-web-bucket/*"
        }
      ]
    }
    ```
  - _Static Website Hosting_ enabled.
    - Set `index.html` for the _Index Document_.
    - Add to _Edit Redirection Rules_ the following (see [_S3 Static Website Hosting Route All Paths to `index.html`_](http://stackoverflow.com/questions/16267339/s3-static-website-hosting-route-all-paths-to-index-html) at Stackoverflow for more information):

    ```xml
    <RoutingRules>
      <RoutingRule>
        <Condition>
          <HttpErrorCodeReturnedEquals>404</HttpErrorCodeReturnedEquals>
        </Condition>
        <Redirect>
          <ReplaceKeyPrefixWith>#/</ReplaceKeyPrefixWith>
        </Redirect>
      </RoutingRule>
    </RoutingRules>
    ```
1. `$ npm run deploy`
  - Will generate production-ready assets.
1. `$ aws s3 sync --delete dist/ s3://my-client-web-bucket/`

## Libraries

- [fetch](https://github.com/github/fetch)
  - A window.fetch JavaScript polyfill.
- [Immutable](https://facebook.github.io/immutable-js/)
  - Immutable persistent data collections for Javascript which increase efficiency and simplicity.
- [Restful.js](https://github.com/marmelab/restful.js)
  - A pure JS client for interacting with server-side RESTful resources. Think Restangular without Angular.
- [Regenerator Loader for Webpack](https://github.com/pjeby/regenerator-loader)

## References

- [React and Webpack Cookbook](http://christianalfoni.github.io/react-webpack-cookbook/)
- [React on ES6+](http://babeljs.io/blog/2015/06/07/react-on-es6-plus/)
- [Jest Example: React ES6](https://github.com/facebook/jest/tree/master/examples/react-es6)
- [Taming the Asynchronous Beast with ES7](http://pouchdb.com/2015/03/05/taming-the-async-beast-with-es7.html)
- [How to Get ES7 Async Functions with Babel](http://jbernier.com/how-to-get-es7-async-functions-with-babel)
