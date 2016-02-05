import React from 'react';
import ReactDOM from 'react-dom';

import Radium, { Style, StyleRoot } from 'radium';
import color from 'color';

import 'font-awesome/css/font-awesome.css';

@Radium
class Header extends React.Component {

  static styles = {
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
        marginLeft: 'auto',
        marginRight: 'auto',

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
    '': {}
  };

  render() {
    return (
      <section>
        <Style scopeSelector="section" rules={Section.styles}/>

        <p>I am an awesome software developer.</p>

      </section>
    );
  }
}

@Radium
class Footer extends React.Component {

  static styles = {
    '': {
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
      margin: 0
    },

    'dl dt': {
      clear: 'both',
      float: 'left',
      margin: '0 0.5em 0 0'
    },

    'dl dd': {
      margin: '0'
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
      fontFamily: 'Helvetica, sans-serif'
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


