import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { Row, Col, Alert, Button, ButtonGroup, Card } from 'reactstrap';
import ToggleSwitch from './toggle-switch';
import TempHumWidget from './TempHumWidget';

import { IRootState } from 'app/shared/reducers';
import { getSession } from 'app/shared/reducers/authentication';
import { set } from 'app/shared/reducers/remote-controller';
import { getEntities as getControllers } from 'app/entities/controller/controller.reducer';

const DHT11 = 'DHT11';

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
  };

  render() {
    const { account, controllerList } = this.props;
    return (
      <Row>
        <Col md="12">
          <h2>
            <Translate contentKey="home.title">Welcome, Java Hipster!</Translate>
          </h2>
          {account && account.login ? (
            <div>
              <p className="lead">
                <Translate contentKey="home.logged.message" interpolate={{ username: account.login }}>
                  You are logged in as user {account.login}.
                </Translate>
              </p>
              {controllerList.length === 0 ?
                <p>
                  <Translate contentKey="home.logged.configuring">
                    Start configuring your raspberry
                  </Translate>
                </p>
                : null
              }
              <Row className="m-1">
                {controllerList.filter(controller => controller.sensorName === DHT11)
                  .map((controller, index) =>
                    <Col key={index} xs="12" sm="6" className="m-2">
                      <Row>
                        <TempHumWidget
                          temperature={20}
                          humidity={50}/>
                      </Row>
                    </Col>
                )}
                { controllerList.length !== 0 &&
                  <Col xs="12" sm={{ size: 5, offset: 1 }} className="shadow-sm m-1" style={{ backgroundColor: '#eee' }}>
                  {controllerList.map((controller, i) => (
                    controller.mode === 'OUTPUT' &&
                    <Row key={`controller-${i}`}>
                      <Col className="mb-2">
                        <span className="align-middle mt-3 m-2 float-left">{controller.name}</span>
                        <span className="align-middle float-right mt-2">
                          <ToggleSwitch
                            on={this.set.bind(this, 'high', controller.pinName)}
                            off={this.set.bind(this, 'low', controller.pinName)}
                            status={controller.state}
                          />
                        </span>
                      </Col>
                    </Row>
                  ))}
                  </Col>
                }
              </Row>
            </div>
          ) : (
            <div>
              <Alert color="warning">
                <Link to="/login" className="alert-link">
                  <Translate contentKey="global.messages.info.authenticated.link"> sign in</Translate>
                </Link>
                <Translate contentKey="global.messages.info.authenticated.suffix">to start control your raspberry</Translate>
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
