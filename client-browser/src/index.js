import React from 'react';
import ReactDOM from 'react-dom';

import Loading from './Loading.js';

class Application extends React.Component {

  constructor() {
    super();
    this.state = {
      component: <Loading/>
    };
  }

  componentDidMount() {

    setTimeout(() => {
      require.ensure([], () => {
        const {Hello} = require('./Hello.js');
        this.setState({
          component: <Hello/>
        });
      });
    }, 1000);

  }

  render() {
    return (
      <div>
        <h1>Michael Ahlers</h1>

        {this.state.component}
      </div>
    );
  }

}

ReactDOM.render(
  <Application/>,
  document.body.appendChild(document.createElement('div'))
);
