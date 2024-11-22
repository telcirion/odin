import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import Register from './components/Register';
import Login from './components/Login';
import PersonList from './components/PersonList';
import PersonForm from './components/PersonForm';

function App() {
  return (
    <Router>
      <div className="App">
        <Switch>
          <Route path="/register" component={Register} />
          <Route path="/login" component={Login} />
          <Route path="/persons" component={PersonList} />
          <Route path="/person-form" component={PersonForm} />
        </Switch>
      </div>
    </Router>
  );
}

export default App;
