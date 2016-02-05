import React from 'react';
import ReactDOM from 'react-dom';

//import Container from './components/Container.js';
//
//const route = {
//
//  component: Container,
//
//  childRoutes: [
//    {
//      path: '/',
//
//      getComponent(location, cb) {
//        require.ensure([], (require) => {
//          cb(null, require('./components/Application.js'));
//        });
//      },
//
//      getChildRoutes(location, cb) {
//        require.ensure([], (require) => {
//          cb(null, [
//            require('./routes/Posts'),
//            require('./routes/Projects')
//          ]);
//        });
//      }
//    }
//  ]
//
//};
//
///* In light of http://stackoverflow.com/q/16267339/700420 forgo using HTML5 browser history until a good solution is found to deploy to S3. See also http://rackt.org/history/stable/HashHistoryCaveats.html for details on (and caveats especially of) hash history. */
//
//import { Router, useRouterHistory } from 'react-router';
//import { createHistory } from 'history';
//
//(async function () {
//
//  const settings = await require('./settings');
//
//  const history = useRouterHistory(createHistory)();
//
//  history.listen(function (location) {
//    const path = (/#(\/.*)$/.exec(location.hash) || [])[1];
//    if (path) history.replace(path);
//  });
//
//  ReactDOM.render(
//    <Router history={history} routes={route}/>,
//    document.body.appendChild(document.createElement('div'))
//  );
//
//})();

import Radium, { Style, StyleRoot } from 'radium';
import color from 'color';

const styles = {
  base: {
    html: {
      fontFamily: 'Helvetica, sans-serif'
    },
    body: {}
  },

  landing: {
    position: 'absolute',
    left: 0,
    top: 0,
    width: '100%',
    height: '100%',
    //backgroundColor: 'blue',
    transformStyle: 'preserve-3d',

    content: {

      position: 'absolute',
      //backgroundColor:'red',
      left: '50%',
      top: '50%',
      marginRight: '-50%',
      transform: 'translate(-50%, -50%)',

      logo: {

        textAlign: 'center',

        width: 300,
        height: 193,
        backgroundImage: `url("${require('./assets/Ahlers Consulting (300 px, 193 px).png')}")`,

        '@media (min-width: 992px)': {
          width: 500,
          height: 321,
          backgroundImage: `url("${require('./assets/Ahlers Consulting (500 px, 321 px).png')}")`
        },

        '@media (min-width: 1200px)': {
          width: 700,
          height: 450,
          backgroundImage: `url("${require('./assets/Ahlers Consulting (700 px, 450 px).png')}")`
        }

      },

      title: {

        textAlign: 'center',

        fontWeight: 'lighter',
        fontSize: '1.75em',
        letterSpacing: '0.1em',

        textTransform: 'lowercase',

        '@media (min-width: 992px)': {
          fontSize: '2.5em',
          letterSpacing: '0.2em'
        },

        '@media (min-width: 1200px)': {
          fontSize: '3em',
          letterSpacing: '0.3em'
        }
      }

    }
  }

};

const foo = {

  'ul': {
    textAlign: 'center',
    margin: 0,
    padding: 0,
    fontSize: '0.75em',
    listStyleType: 'none'
  },

  'ul li': {
    margin: 0
  },

  mediaQueries: {
    '(min-width: 992px)': {
      'ul': {
        fontSize: '1em'
      },

      'ul li': {
        display: 'inline',
        margin: '0 0.5em'
      }
    },

    '(min-width: 1200px)': {
      'ul': {
        fontSize: '1.5em'
      },

      'ul li': {
        display: 'inline',
        margin: '0 0.75em'
      }
    }
  },

  'a': {
    color: 'gray',
    textDecoration: 'none'
  }

};

@Radium
class Landing extends React.Component {

  render() {
    return (
      <div style={styles.landing}>
        <div style={styles.landing.content}>
          <Style rules={foo}/>

          <div style={styles.landing.content.logo}></div>
          <h1 style={styles.landing.content.title}>Ahlers Consulting</h1>
          <ul>
            <li><a href="mailto:michael@ahlers.consulting">michael@ahlers.consulting</a></li>
            <li><a href="tel:1 (571) 830-0258">1 (571) 830-0258</a></li>
          </ul>
        </div>
      </div>
    );
  }

}

ReactDOM.render(
  <StyleRoot>
    <Style rules={styles.base}/>

    <Landing/>
  </StyleRoot>,
  document.body.appendChild(document.createElement('div'))
);


