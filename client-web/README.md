# Michael Ahlers Presence Web Client

## Prerequisites

### OS X

1. Follow installation instructions for [Homebrew](http://brew.sh/).
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
1. `$ npm run deploy`
  - Will generate production-ready assets.
1. `$ aws sync --delete dist/ s3://my-client-web-bucket/`
    
## Libraries

- [Restful.js](https://github.com/marmelab/restful.js)

## References

- [React and Webpack Cookbook](http://christianalfoni.github.io/react-webpack-cookbook/)
