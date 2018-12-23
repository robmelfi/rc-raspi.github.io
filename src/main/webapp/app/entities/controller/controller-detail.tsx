import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './controller.reducer';
import { IController } from 'app/shared/model/controller.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IControllerDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ControllerDetail extends React.Component<IControllerDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { controllerEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="rcraspiApp.controller.detail.title">Controller</Translate> [<b>{controllerEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="rcraspiApp.controller.name">Name</Translate>
              </span>
            </dt>
            <dd>{controllerEntity.name}</dd>
            <dt>
              <span id="mode">
                <Translate contentKey="rcraspiApp.controller.mode">Mode</Translate>
              </span>
            </dt>
            <dd>{controllerEntity.mode}</dd>
            <dt>
              <span id="state">
                <Translate contentKey="rcraspiApp.controller.state">State</Translate>
              </span>
            </dt>
            <dd>{controllerEntity.state ? 'high' : 'low'}</dd>
            <dt>
              <Translate contentKey="rcraspiApp.controller.pin">Pin</Translate>
            </dt>
            <dd>{controllerEntity.pinName ? controllerEntity.pinName : ''}</dd>
            <dt>
              <Translate contentKey="rcraspiApp.controller.sensor">Sensor</Translate>
            </dt>
            <dd>{controllerEntity.sensorName ? controllerEntity.sensorName : ''}</dd>
            <dt>
              <Translate contentKey="rcraspiApp.controller.timer">Timer</Translate>
            </dt>
            <dd>{controllerEntity.timerName ? controllerEntity.timerName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/controller" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/controller/${controllerEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ controller }: IRootState) => ({
  controllerEntity: controller.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ControllerDetail);
