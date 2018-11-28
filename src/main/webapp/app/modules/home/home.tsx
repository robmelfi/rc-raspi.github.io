import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { Row, Col, Alert, Button, ButtonGroup, Card } from 'reactstrap';
import ToggleSwitch from './toggle-switch';

import { IRootState } from 'app/shared/reducers';
import { getSession } from 'app/shared/reducers/authentication';
import { set } from 'app/shared/reducers/remote-controller';
import { getEntities as getControllers } from 'app/entities/controller/controller.reducer';

export interface IHomeProp extends StateProps, DispatchProps {}

export class Home extends React.Component<IHomeProp> {
  componentDidMount() {
    this.props.getSession();
    if (this.props.isAuthenticated) {
      this.props.getControllers();
    }
  }

  componentDidUpdate(prevProps: IHomeProp, prevState) {
    if (this.props.account !== prevProps.account) {
      this.props.getControllers();
    }
  }

  set = (type, pin) => {
    this.props.set(type, pin);
    this.props.getControllers();
  };

  render() {
    const { account, controllerList } = this.props;
    return (
      <Row>
        <Col md="12">
          <h2>
            <Translate contentKey="home.title">Welcome, Java Hipster!</Translate>
          </h2>
          <p className="lead">
            <Translate contentKey="home.subtitle">This is your homepage</Translate>
          </p>
          {account && account.login ? (
            <div>
              <Alert color="success">
                <Translate contentKey="home.logged.message" interpolate={{ username: account.login }}>
                  You are logged in as user {account.login}.
                </Translate>
              </Alert>
              {controllerList.map((controller, i) => (
                  <Row key={`controller-${i}`}>
                    <Col className="border m-1 p-1">
                      <span className="align-middle ml-2">{controller.name}</span>
                      <span className="align-middle float-right">
                        <ToggleSwitch
                          on={this.set.bind(this, 'high', controller.pinName)}
                          off={this.set.bind(this, 'low', controller.pinName)}
                          status={controller.status} />
                      </span>
                    </Col>
                  </Row>
              ))}
            </div>
          ) : (
            <div>
              <Alert color="warning">
                <Link to="/login" className="alert-link">
                  <Translate contentKey="global.messages.info.authenticated.link"> sign in</Translate>
                </Link>
                <Translate contentKey="global.messages.info.authenticated.suffix">
                  to start control your raspberry
                </Translate>
              </Alert>

              <Alert color="warning">
                <Translate contentKey="global.messages.info.register.noaccount">You do not have an account yet?</Translate>
                &nbsp;
                <Link to="/register" className="alert-link">
                  <Translate contentKey="global.messages.info.register.link">Register a new account</Translate>
                </Link>
              </Alert>
            </div>
          )}
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated,
  controllerList: storeState.controller.entities
});

const mapDispatchToProps = {
  getSession,
  set,
  getControllers
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Home);
