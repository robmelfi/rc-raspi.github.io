import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './timer.reducer';
import { ITimer } from 'app/shared/model/timer.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITimerDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class TimerDetail extends React.Component<ITimerDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  handleClose = () => {
    this.props.history.goBack();
  };

  render() {
    const { timerEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="rcraspiApp.timer.detail.title">Timer</Translate>
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="rcraspiApp.timer.name">Name</Translate>
              </span>
            </dt>
            <dd>{timerEntity.name}</dd>
            <dt>
              <span id="start">
                <Translate contentKey="rcraspiApp.timer.start">Start</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={timerEntity.start} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="stop">
                <Translate contentKey="rcraspiApp.timer.stop">Stop</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={timerEntity.stop} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="repeat">
                <Translate contentKey="rcraspiApp.timer.repeat">Repeat</Translate>
              </span>
            </dt>
            <dd><Translate contentKey={`rcraspiApp.Repeat.${timerEntity.repeat}`}>Repeat</Translate></dd>
          </dl>
          <Button onClick={this.handleClose} replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/timer/${timerEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ timer }: IRootState) => ({
  timerEntity: timer.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TimerDetail);
