import React, { useState } from 'react';

import { Grid, Box, Paper, Button, Typography, Dialog, Backdrop } from '@material-ui/core';

import waiting from '../assets/images/waiting.png';

import backdropImage from '../assets/images/i-graph.jpg'; // Import your background image
const Popup = (props) => {

    const [showModal, setShowModal] = useState(true);

 

    const handleModalClose = () => {

        setShowModal(false);

    }

 

    const handleModalOk = () => {

       // alert("Submit Success!\nThank you")

        props.history.push("/")

    }

 

    return (

        <>

            <Dialog open={showModal} onClose={handleModalClose} aria-labelledby="login-dialog-title">

            <Backdrop style={{
                 backgroundImage: `url(${backdropImage})`,backgroundSize: 'cover',
              //   backgroundColor:'',
             backgroundRepeat: 'no-repeat'}} open={showModal} onClick={handleModalClose} />
                <Paper elevation={0} style={{ padding: 0, margin: '60px auto', width: 400 }}>

                    <Grid item>

                        {

                            <center>

                                <>

                                    <img src={waiting} width="125" height="120" style={{ display: 'block', border: '0px' }} />

                                </>

                                <br />

                                <Typography variant="h6">

                                    Your credit score is low we need additional documentation. Your application is under process. Soon, you will be notified about it.

                                </Typography>

                            </center>

                        }

                        <Box mt={2}>

                            <center>

                                <Button type="submit" variant="contained" color="primary" halfWidth onClick={handleModalOk}>

                                    Ok

                                </Button>

                            </center>

                        </Box>

                    </Grid>

                </Paper>

            </Dialog>

        </>

    );

};

 

export default Popup;