import React from 'react';
import Radium,{Style} from 'radium';
import color from 'color';


var pulseKeyframes = Radium.keyframes({
  '0%': {transform: 'scale3d(1,1,0)'},
  '25%': {transform: 'scale3d(0.98,0.98,0)'},
  '75%': {transform: 'scale3d(1.02,1.02,0)'},
  '100%': {transform: 'scale3d(1,1,0)'}
}, 'pulse');

const styles = {
  base: {
    background: 'blue',
    border: 0,
    borderRadius: 4,
    color: 'white',
    padding: '1.5em',

    // Use a placeholder animation name in `animation`
    animation: 'linear 500ms infinite',
    // Assign the result of `keyframes` to `animationName`
    animationName: pulseKeyframes,

    ':hover': {
      background: 'red'
    },

    ':focus': {
      background: 'green'
    },

    ':active': {
      background: 'yellow'
    }
  },

  primary: {
    background: '#0074D9'
  },

  warning: {
    background: '#FF4136'
  }
};

@Radium
class Button extends React.Component {
  static propTypes = {
    kind: React.PropTypes.oneOf(['primary', 'warning']).isRequired
  };

  render() {
    // Radium extends the style attribute to accept an array. It will merge
    // the styles in order. We use this feature here to apply the primary
    // or warning styles depending on the value of the `kind` prop. Since its
    // all just JavaScript, you can use whatever logic you want to decide which
    // styles are applied (props, state, context, etc).
    return (
      <button
        style={[
          styles.base,
          styles[this.props.kind]
        ]}>
        {this.props.children}
      </button>
    );
  }
}

module.exports = Button;
