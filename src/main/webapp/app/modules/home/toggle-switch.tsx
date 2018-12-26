import React from 'react';
import { Button, ButtonGroup } from 'reactstrap';

const toggleSwitch = props =>
  <ButtonGroup>
    <Button
      color="primary"
      onClick={props.off}
      disabled={!props.status}>ON</Button>
    <Button
      color="primary"
      onClick={props.on}
      disabled={props.status}>OFF</Button>
  </ButtonGroup>;
export default toggleSwitch;
