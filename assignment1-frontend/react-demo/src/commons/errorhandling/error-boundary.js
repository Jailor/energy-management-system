import React, { useState, useEffect } from 'react';
import styles from '../styles/project-style.css';

const ErrorBoundary = (props) => {
    const [hasError, setHasError] = useState(false);
  
    useEffect(() => {
      const componentDidCatch = (error, info) => {
        setHasError(true);
        console.log("error:" + error);
        console.log("info:" + info);
      };
  
      // Add componentDidCatch as an event listener
      window.addEventListener('error', componentDidCatch);
  
      // Cleanup the event listener on unmount
      return () => {
        window.removeEventListener('error', componentDidCatch);
      };
    }, []);
  
    if (hasError) {
      return <h1 className={styles.errorTitle}>An error occurred at the component level.</h1>;
    }
  
    return props.children;
  };
  
  export default ErrorBoundary;
