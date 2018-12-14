import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
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

  handleBack = () => {
    this.props.history.goBack();
  };

  render() {
    const { sensorEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="rcraspiApp.sensor.detail.title">Sensor</Translate> [<b>{sensorEntity.name}</b>]
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
            <dd>
              {sensorEntity.imagePath ? (
                <div>
                  <a href={sensorEntity.imagePath}>
                    <img src={sensorEntity.imagePath} style={{ maxHeight: '200px' }} />
                  </a>
                </div>
              ) : null}
            </dd>
          </dl>
          <Button onClick={this.handleBack} replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
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
