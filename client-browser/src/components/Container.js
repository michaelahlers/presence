import React from 'react';

import Radium, { Style, StyleRoot } from 'radium';

const rules = {

  body: {
    margin: 0,
    fontFamily: 'Helvetica, Arial, sans-serif'
  },

  html: {
    background: '#ccc',
    fontSize: '100%'
  },

  mediaQueries: {

    '(min-width: 550px)': {
      html: {
        fontSize: '120%'
      }
    },

    '(min-width: 1200px)': {
      html: {
        fontSize: '140%'
      }
    }

  }

};

@Radium
class Container extends React.Component {

  render() {
    return (
      <StyleRoot>

        <Style rules={{rules}}/>

        {this.props.children}

      </StyleRoot>
    );
  }

}
;

module.exports = Container;
