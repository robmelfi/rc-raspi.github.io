import './pin.scss';

import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './pin.reducer';
import { IPin } from 'app/shared/model/pin.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPinProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Pin extends React.Component<IPinProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { pinList, match } = this.props;
    return (
      <div>
        <h2 id="pin-heading">
          <Translate contentKey="rcraspiApp.pin.home.title">Pins</Translate>
        </h2>
        <div className="table-responsive">
          <Row>
            <Col xs="12" sm="8">
              <span className="pin-header rounded" />
            </Col>
            <Col xs="12" sm="4">
              <Table responsive striped size="sm">
                <thead>
                <tr>
                  <th>
                    <Translate contentKey="rcraspiApp.pin.name">Name</Translate>
                  </th>
                </tr>
                </thead>
                <tbody>
                {pinList.map((pin, i) => (
                  <tr key={`entity-${i}`}>
                    <td>{pin.name}</td>
                  </tr>
                ))}
                </tbody>
              </Table>
            </Col>
          </Row>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ pin }: IRootState) => ({
  pinList: pin.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Pin);
