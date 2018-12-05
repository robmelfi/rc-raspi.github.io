import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './sensor.reducer';
import { ISensor } from 'app/shared/model/sensor.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISensorDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class SensorDetail extends React.Component<ISensorDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { sensorEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="rcraspiApp.sensor.detail.title">Sensor</Translate> [<b>{sensorEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="rcraspiApp.sensor.name">Name</Translate>
              </span>
            </dt>
            <dd>{sensorEntity.name}</dd>
            <dt>
              <span id="description">
                <Translate contentKey="rcraspiApp.sensor.description">Description</Translate>
              </span>
            </dt>
            <dd>{sensorEntity.description}</dd>
            <dt>
              <span id="image">
                <Translate contentKey="rcraspiApp.sensor.image">Image</Translate>
              </span>
            </dt>
            <dd>
              {sensorEntity.image ? (
                <div>
                  <a onClick={openFile(sensorEntity.imageContentType, sensorEntity.image)}>
                    <img src={`data:${sensorEntity.imageContentType};base64,${sensorEntity.image}`} style={{ maxHeight: '30px' }} />
                  </a>
                  <span>
                    {sensorEntity.imageContentType}, {byteSize(sensorEntity.image)}
                  </span>
                </div>
              ) : null}
            </dd>
          </dl>
          <Button tag={Link} to="/entity/sensor" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/sensor/${sensorEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ sensor }: IRootState) => ({
  sensorEntity: sensor.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SensorDetail);
