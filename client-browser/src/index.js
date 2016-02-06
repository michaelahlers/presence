import React from 'react';
import ReactDOM from 'react-dom';

import Radium, { Style, StyleRoot } from 'radium';
import color from 'color';

import 'font-awesome/css/font-awesome.css';

@Radium
class Header extends React.Component {

  static styles = {
    '': {
      padding: '2em 0 0 0'
    },

    'h1': {
      textAlign: 'center',
      fontWeight: 'lighter',
      fontSize: '2em',
      textTransform: 'lowercase',
      letterSpacing: '0.25em',
      color: 'gray'
    },

    /* TODO: Genericize this function. */
    logo() {
      const width = 30;
      const height = width / 1.5;
      const unit = 'em';

      return {
        margin: '0 auto',

        width: `${width}${unit}`,
        height: `${height}${unit}`,

        backgroundImage: `url("${require('./assets/Ahlers Consulting Logo.png')}")`,
        backgroundSize: `${width}${unit} ${height}${unit}`,

        '@media (min-device-pixel-ratio: 1.5)': {
          backgroundImage: `url("${require('./assets/Ahlers Consulting Logo@2x.png')}")`,
          backgroundSize: `${width}${unit} ${height}${unit}`
        }
      };
    }
  };

  render() {
    return (
      <header>
        <Style scopeSelector="header" rules={Header.styles}/>

        <div style={Header.styles.logo()}></div>
        <h1>Ahlers Consulting</h1>
      </header>
    );
  }

}

@Radium
class Section extends React.Component {

  static styles = {
    '': {
      padding: '2em 2em'
    },

    mediaQueries: {
      '(min-width: 992px)': {
        '': {
          padding: '2em 10em'
        }
      },
      '(min-width: 1200px)': {
        '': {
          padding: '2em 20em'
        }
      }
    },

    'p': {
      fontSize: '1.5em'
    }
  };

  render() {
    return (
      <section>
        <Style scopeSelector="section" rules={Section.styles}/>

        <p>Michael Ahlers is software developer—with over {new Date().getFullYear() - 2001} years of professional experience—who views the practice through an
          engineering lens, and offers tenacious attention to detail. When not working, Michael enjoys competitive bike racing, and recreational flying as a
          certified sport pilot.</p>

      </section>
    );
  }
}

@Radium
class Footer extends React.Component {

  static styles = {
    '': {
      padding: '0 2em',
      background: '#222',
      color: 'gray'
    },

    'a': {
      color: 'gray'
    },

    'a:hover': {
      color: 'white'
    },

    'dl': {
      position: 'relative',
      left: '50%',
      transform: 'translateX(-50%)',
      fontSize: '1.5em',
      display: 'inline-block'
    },

    'dl dt': {
      clear: 'left',
      float: 'left',
      margin: 0
    },

    'dl dd': {
      margin: '0 0 0 1.25em'
    },

    'dl dt, dl dd': {
      lineHeight: '1.5em'
    },

    'dl dd:after': {
      content: '',
      display: 'table',
      clear: 'both'
    }
  };

  render() {
    return (
      <footer>
        <Style scopeSelector="footer" rules={Footer.styles}/>

        <dl>
          <dt><i className="fa fa-envelope-square"/></dt>
          <dd><a href="mailto:michael@ahlers.consulting?subject=Found at ahlers.consulting.">michael@ahlers.consulting</a></dd>
          <dt><i className="fa fa-linkedin-square"/></dt>
          <dd><a href="https://linkedin.com/in/michael-ahlers-52719358" target="_blank">michael-ahlers-52719358</a></dd>
          <dt><i className="fa fa-phone-square"/></dt>
          <dd><a href="tel:+1-571-830-0258">+1 (571) 830-0258</a></dd>
        </dl>

      </footer>
    );
  }
}

@Radium
class Application extends React.Component {

  static styles = {
    'html': {
      margin: 0
    },

    'body': {
      margin: 0,
      padding: 0,
      fontFamily: 'Helvetica, sans-serif',
      fontSize: '100%'
    },

    'html, body, body > div, body > div > div': {
      height: '100%'
    },

    'a': {
      textDecoration: 'none'
    },

    'article': {
      display: 'flex',
      flexFlow: 'column',
      height: '100%'
    },

    'article header': {
      flex: '0 1 auto'
    },

    'article section': {
      flex: '1 1 auto'
    },

    'article footer': {
      flex: '0 1 auto'
    }
  };

  render() {
    return (
      <StyleRoot>
        <Style rules={Application.styles}/>

        <article>
          <Header/>
          <Section/>
          <Footer/>
        </article>
      </StyleRoot>
    );
  }

}

ReactDOM.render(
  <Application />,
  document.body.appendChild(document.createElement('div'))
);


