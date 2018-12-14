import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './timer.reducer';
import { ITimer } from 'app/shared/model/timer.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITimerProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Timer extends React.Component<ITimerProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { timerList, match } = this.props;
    return (
      <div>
        <h2 id="timer-heading">
          <Translate contentKey="rcraspiApp.timer.home.title">Timers</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />&nbsp;
            <Translate contentKey="rcraspiApp.timer.home.createLabel">Create new Timer</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="rcraspiApp.timer.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="rcraspiApp.timer.start">Start</Translate>
                </th>
                <th>
                  <Translate contentKey="rcraspiApp.timer.stop">Stop</Translate>
                </th>
                <th>
                  <Translate contentKey="rcraspiApp.timer.repeat">Repeat</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {timerList.map((timer, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${timer.id}`} color="link" size="sm">
                      {timer.id}
                    </Button>
                  </td>
                  <td>{timer.name}</td>
                  <td>
                    <TextFormat type="date" value={timer.start} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={timer.stop} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    <Translate contentKey={`rcraspiApp.Repeat.${timer.repeat}`} />
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${timer.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${timer.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${timer.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ timer }: IRootState) => ({
  timerList: timer.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Timer);
